# 优惠券分发模块
## 实体设计
### 用户优惠券实体
类实体
```java
public class UserCoupon extends BaseDO {

    /**
     * 主键 ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户 ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 优惠券模板 ID
     */
    @TableField(value = "coupon_template_id")
    private Long couponTemplateId;

    /**
     * 店铺编号
     * 如果是店铺券：存储当前店铺编号
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 优惠对象:
     * 0：指定商品可用，1：全店通用
     */
    @TableField(value = "target")
    private DiscountTargetEnum target;

    /**
     * 优惠类型：
     * 0：立减券 1：满减券 2：折扣券 3：随机券
     */
    @TableField(value = "type")
    private DiscountTypeEnum type;

    /**
     * 立减券：立减金额
     * 满减券：满足条件后立减的金额
     * 折扣券：折扣率
     * 随机券：随机金额
     */
    @TableField(value = "face_value")
    private BigDecimal faceValue;

    /**
     * 满减券门槛金额（仅满减券有效）
     */
    @TableField(value = "min_amount")
    private BigDecimal minAmount;

    /**
     * 领取时间
     */
    @TableField(value = "receive_time")
    private Date receiveTime;

    /**
     * 使用时间
     */
    @TableField(value = "use_time")
    private Date useTime;

    /**
     * 有效期开始时间
     */
    @TableField(value = "valid_start_time")
    private Date validStartTime;

    /**
     * 有效期结束时间
     */
    @TableField(value = "valid_end_time")
    private Date validEndTime;

    /**
     * 优惠券使用状态：
     * 0-未使用 1-锁定 2-已使用 3-已过期 4-已撤回
     */
    @TableField(value = "status")
    private UserCouponStatusEnum status;

}
```

数据库实体
```sql
create table t_user_coupon
(
    id                 bigint                             not null comment '主键ID'
        primary key,
    user_id            bigint                             not null comment '用户ID',
    coupon_template_id bigint                             not null comment '优惠券模板ID',
    shop_id            bigint                             null comment '店铺编号，如果是店铺券：存储当前店铺编号',
    target             tinyint                            not null comment '优惠对象: 0-指定商品可用 1-全店通用',
    type               tinyint                            not null comment '优惠类型: 0-立减券 1-满减券 2-折扣券 3-随机券',
    face_value         decimal(10, 2)                     not null comment '立减金额 / 满减立减金额 / 折扣率 / 随机金额',
    min_amount         decimal(10, 2)                     null comment '满减门槛金额，仅满减券有效',
    receive_time       datetime                           not null comment '领取时间',
    use_time           datetime                           null comment '使用时间',
    valid_start_time   datetime                           not null comment '有效期开始时间',
    valid_end_time     datetime                           not null comment '有效期结束时间',
    status             tinyint  default 0                 not null comment '状态: 0-未使用 1-锁定 2-已使用 3-已过期 4-已撤回',
    create_time        datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time        datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    create_by          varchar(50)                        not null comment '创建人',
    update_by          varchar(50)                        not null comment '修改人',
    del_flag           tinyint  default 0                 null comment '逻辑删除标记：0-未删除，1-删除'
)
    comment '用户优惠券表';
```
**说明：用户优惠券表主要用来保存用户领取的优惠券信息，包括优惠券模板ID、优惠券使用状态、领取时间、使用时间、有效期开始时间、有效期结束时间等。**
**注意：用户领取完优惠券后，优惠券模板的是否过期、是否有效等就与用户领取的优惠券状态无关了，用户领取的优惠券状态只跟用户是否使用、有效期是否过期有关。**
**分发任务分发优惠券给用户、用户主动领取优惠券本质都是在t_user_coupon中插入记录。用户多次领取同一张优惠券，会插入多条记录，领取到的每张优惠券的生命周期独立。**
**根据优惠券模板信息插入用户优惠券表时，直接根据规则将对应字段进行填充，用户使用券时直接读取用户优惠券表即可。**

### 分发记录实体
类实体
```java
public class DistributionRecord {

    /**
     * 用户 ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 分发任务 ID
     */
    @TableField(value = "distribution_task_id")
    private Long distributionTaskId;


}
```

数据库实体
```sql
create table t_distribution_record
(
    task_id bigint not null comment '分发任务ID',
    user_id bigint not null comment '用户ID',
    primary key (task_id, user_id)
)
    comment '优惠券分发记录表';
```

**说明：分发记录表主要用来做唯一性校验，保证每次分发任务中的用户只分发一次优惠券。**

## 业务设计
### 分发优惠券给用户

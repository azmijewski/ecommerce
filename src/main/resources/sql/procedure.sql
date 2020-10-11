DELIMITER $$
create procedure get_old_carts_id(
	IN currentDate datetime
)
begin
    select c.id
    from cart c
    where timestampdiff(MINUTE, c.updated_at, currentDate)> 15;
end $$
DELIMITER ;

DELIMITER $$
create procedure get_expire_orders_waiting_for_payment(
IN currentDate datetime
)
begin
	select id
    from orders
    where order_status = 'WAITING_FOR_PAYMENT'
    and timestampdiff(DAY, currentDate, created_at) >= 14;
end $$
DELIMITER ;

DELIMITER $$
create procedure get_orders_waiting_for_payment_to_expire_in(
IN currentDate datetime,
IN daysToExpire int
)
begin
	select * from orders o
	join user u on o.user_id = u.id
	join order_product op on op.order_id = o.id
	join product p on op.product_id = p.id
	where o.order_status = 'WAITING_FOR_PAYMENT'
	and 14 - timestampdiff(DAY, currentDate, created_at) = daysToExpire;
end $$
DELIMITER ;
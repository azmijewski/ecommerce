DELIMITER $$
create procedure get_old_carts_id(
	IN currentDate datetime
)
begin
 select id from cart where (updated_at is null and minute(created_at) > 15) or (updated_at is not null and minute(updated_at) > 15);
 end$$
DELIMITER ;

drop procedure get_old_carts;
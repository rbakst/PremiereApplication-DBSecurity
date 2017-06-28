-- ================================================
-- Template generated from Template Explorer using:
-- Create Trigger (New Menu).SQL
--
-- Use the Specify Values for Template Parameters 
-- command (Ctrl-Shift-M) to fill in the parameter 
-- values below.
--
-- See additional Create Trigger templates for more
-- examples of different Trigger statements.
--
-- This block of comments will not be included in
-- the definition of the function.
-- ================================================
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE TRIGGER ProcessOrder  on Order_line
   AFTER INSERT,DELETE,UPDATE
   AS 
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for trigger here

	--declare necessary variables
	declare @Action char(1);
	declare @qty smallint;
	declare @price smallmoney;
	declare @partID nchar(10);
	declare @orderID nchar(10);
	declare @custid nchar(10);
	declare @qtyonhand smallint;
	declare @remaincredit smallmoney;

	--1. verify type of operation
	--2. verify that you have enough inventory of the part being ordered
	--3. verify that customer has enough credit to cover cost of the order

	--4. update the inventory level of the part being ordered
	--5. update the balance of the customer placing the order

	  --determine which type of action has occurred
    SET @Action = (CASE WHEN EXISTS(SELECT * FROM INSERTED)
                         AND EXISTS(SELECT * FROM DELETED)
                        THEN 'U'  -- Set Action to Updated.
                        WHEN EXISTS(SELECT * FROM INSERTED)
                        THEN 'I'  -- Set Action to Insert.
                        WHEN EXISTS(SELECT * FROM DELETED)
                        THEN 'D'  -- Set Action to Deleted.
                        ELSE NULL -- Skip. It may have been a "failed delete".   
                    END)
--only execute if either of these two fields have been modified
if Update(qty_ordered) or Update(quoted_price)
--start block of code
begin
     if @Action = 'D'  or @Action = 'U'    --deleting part of an order, reverse the entry
	     begin
		     select @qty = qty_ordered from DELETED;
			 select @price = quoted_price from DELETED;
			 select @partid = part_num from DELETED;
			 select @orderid = order_num from DELETED;
			 -- now update the part table, restore the inventory
			 update part
			 set units_on_hand = units_on_hand + @qty
			 where part_num = @partid;

			 --now reduce the balance owed by customer
			 select @custid = cust_num from
			 orders where
			 order_num = @orderid;

			 update customer
			 set cust_balance = cust_balance - @qty*@price
			 where cust_num = @custid
		end    --update or delete
	  if @Action = 'U' or @Action = 'I'    --inserting part of an order
	    begin
		     select @qty = qty_ordered from INSERTED;
			 select @price = quoted_price from INSERTED;
			 select @partid = part_num from INSERTED
			 select @orderid = order_num from inserted
			 select @custid = cust_num from orders
			  where order_num = @orderid;

			 --verify that you have enough inventory 
			 select @qtyonhand = units_on_hand from part
			 where part_num = @partid 
			 if @qtyonhand < @qty   --not enough in stock
			    RAISERROR ('insufficient stock',11,1);
			 --verify that customer has enough credit
			 select @remaincredit = credit_limit - cust_balance 
			 from customer
			 where cust_num = @custid

			 if @qty *@price  > @remaincredit
			    RAISERROR('insufficient credit', 11,1);

				--otherwise continue and process the record
              update part 
			  set units_on_hand = units_on_hand - @qty
			  where part_num = @partid

			  update customer
			  set cust_balance = cust_balance + (@qty * @price)
			  where cust_num = @custid
		end   -- update delete or insert
		
end



    
END   --end the trigger
GO

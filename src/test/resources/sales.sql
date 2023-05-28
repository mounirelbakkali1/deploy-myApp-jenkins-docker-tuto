INSERT INTO categories (id, name) VALUES (2, 'category 1');
INSERT INTO public.products(
    id, name, purchase_price, quantity, reference, category_id)
VALUES (2, 'product 1', 100, 99, "e-56", 2);
INSERT INTO public.sales(
    commission_guide, commission_guide_value, commission_vendor, commission_vendor_value, date_of_sale, delivery_expenses, is_delivery_inclusion, payment_method, purchase_price, product_quantity, selling_price, profit, guide_id, product_id, vendor_id)
VALUES ( 40 , 33 , 10 , 9 , now(),0 , false, "CASH",133,1,199,30,1,1,1);
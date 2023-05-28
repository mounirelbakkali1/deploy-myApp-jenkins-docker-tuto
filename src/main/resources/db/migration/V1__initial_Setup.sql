
CREATE TABLE public.agents (
   id bigserial primary key ,
   agent_type character varying(255),
   name character varying(255),
   phone_number character varying(255),
unique (name)
);





CREATE TABLE public.authorities (
    id bigserial primary key ,
    authority character varying(255),
    user_id bigint
);





CREATE TABLE public.categories (
    id BIGSERIAL PRIMARY KEY,
    name character varying(255) NOT NULL,
    unique (name)
);



CREATE TABLE public.products (
 id bigserial primary key ,
 name character varying(255),
 purchase_price numeric(10,2) NOT NULL,
 quantity integer NOT NULL,
 reference character varying(255),
 category_id bigint NOT NULL,
unique (reference),
unique (name)

);



CREATE TABLE public.sales (
  id bigserial primary key ,
  commission_guide numeric(38,2) NOT NULL,
  commission_guide_value numeric(38,2) NOT NULL,
  commission_vendor numeric(38,2) NOT NULL,
  commission_vendor_value numeric(38,2) NOT NULL,
  date_of_sale date DEFAULT now(),
  delivery_expenses numeric(38,2),
  is_delivery_inclusion boolean DEFAULT false NOT NULL,
  payment_method character varying(255),
  purchase_price numeric(38,2) NOT NULL,
  product_quantity integer NOT NULL,
  selling_price numeric(10,2) NOT NULL,
  profit numeric(38,2) NOT NULL,
  guide_id bigint,
  product_id bigint NOT NULL,
  vendor_id bigint
);




CREATE TABLE public.users (
      id bigserial primary key ,
      enabled boolean NOT NULL,
      name character varying(255),
      password character varying(255),
      role character varying(255),
      username character varying(255),
      unique (username)
);


CREATE TABLE public.users_user_authorities (
   users_id bigserial primary key ,
   user_authorities_id bigint NOT NULL
);









ALTER TABLE ONLY public.sales
    ADD CONSTRAINT fkcs380rsrt1a4j1pijfyfc12p1 FOREIGN KEY (vendor_id) REFERENCES public.agents(id);



ALTER TABLE ONLY public.users_user_authorities
    ADD CONSTRAINT fkhxqb2qiyrp98n55561jokyh0c FOREIGN KEY (user_authorities_id) REFERENCES public.authorities(id);



ALTER TABLE ONLY public.users_user_authorities
    ADD CONSTRAINT fkjo0ccl3ejjj4w7e6f9dkv5wq5 FOREIGN KEY (users_id) REFERENCES public.users(id);




ALTER TABLE ONLY public.authorities
    ADD CONSTRAINT fkk91upmbueyim93v469wj7b2qh FOREIGN KEY (user_id) REFERENCES public.users(id);


ALTER TABLE ONLY public.sales
    ADD CONSTRAINT fkkxc13g7l4ioljxqyoo15nh051 FOREIGN KEY (product_id) REFERENCES public.products(id);


ALTER TABLE ONLY public.products
    ADD CONSTRAINT fkog2rp4qthbtt2lfyhfo32lsw9 FOREIGN KEY (category_id) REFERENCES public.categories(id);




ALTER TABLE ONLY public.sales
    ADD CONSTRAINT fkqxuxrlv7m9at2ncqlvtllfup0 FOREIGN KEY (guide_id) REFERENCES public.agents(id);



-- -------------------------------------------------------------
-- -------------------------------------------------------------
-- TablePlus 1.1.2
--
-- https://tableplus.com/
--
-- Database: postgres
-- Generation Time: 2024-02-27 23:10:29.512949
-- -------------------------------------------------------------

DROP TABLE "public"."m_customer";


-- This script only contains the table creation statements and does not fully represent the table in database. It's still missing: indices, triggers. Do not use it as backup.

-- Table Definition
CREATE TABLE "public"."m_customer" (
                                       "id" varchar NOT NULL,
                                       "name" varchar NOT NULL,
                                       "phone_number" varchar NOT NULL,
                                       PRIMARY KEY ("id")
);

DROP TABLE "public"."m_menu";


-- This script only contains the table creation statements and does not fully represent the table in database. It's still missing: indices, triggers. Do not use it as backup.

-- Table Definition
CREATE TABLE "public"."m_menu" (
                                   "id" varchar NOT NULL,
                                   "name" varchar NOT NULL,
                                   "price" int4,
                                   PRIMARY KEY ("id")
);

DROP TABLE "public"."m_table";


-- This script only contains the table creation statements and does not fully represent the table in database. It's still missing: indices, triggers. Do not use it as backup.

-- Table Definition
CREATE TABLE "public"."m_table" (
                                    "id" varchar NOT NULL,
                                    "name" bpchar NOT NULL,
                                    PRIMARY KEY ("id")
);

DROP TABLE "public"."m_trans_type";


-- This script only contains the table creation statements and does not fully represent the table in database. It's still missing: indices, triggers. Do not use it as backup.

-- Table Definition
CREATE TABLE "public"."m_trans_type" (
                                         "id" varchar NOT NULL,
                                         "description" varchar NOT NULL,
                                         PRIMARY KEY ("id")
);

DROP TABLE "public"."t_bill";


-- This script only contains the table creation statements and does not fully represent the table in database. It's still missing: indices, triggers. Do not use it as backup.

-- Table Definition
CREATE TABLE "public"."t_bill" (
                                   "id" varchar NOT NULL,
                                   "trans_date" date NOT NULL,
                                   "customer_id" varchar,
                                   "table_id" varchar,
                                   "trans_type" varchar NOT NULL,
                                   PRIMARY KEY ("id")
);

DROP TABLE "public"."t_bill_detail";


-- This script only contains the table creation statements and does not fully represent the table in database. It's still missing: indices, triggers. Do not use it as backup.

-- Table Definition
CREATE TABLE "public"."t_bill_detail" (
                                          "id" varchar NOT NULL,
                                          "bill_id" varchar,
                                          "menu_id" varchar,
                                          "qty" int4 NOT NULL,
                                          "price" int4 NOT NULL,
                                          PRIMARY KEY ("id")
);

INSERT INTO "public"."m_customer" ("id","name","phone_number") VALUES
                                                                   ('fa77708b-1294-461a-85e4-762268fbdec1','Kadir','0877123333'),
                                                                   ('a538ec79-b381-4b5c-83ef-049d8972f7fd','Basuki','0812399123'),
                                                                   ('46e0c194-2f4b-4e3a-a999-3b6d82302671','Munaroh','0888901920'),
                                                                   ('0ebd43b3-4027-4891-80cc-6277fb8caba5','Adinda','0815343411'),
                                                                   ('ceb87e94-8667-4d82-a30b-99ad093695e4','Rozak','0857129823'),
                                                                   ('5928c712-bfb8-4614-bd70-bdadf2107535','Devi','0877745983'),
                                                                   ('2bf91858-210b-4d0f-a22d-685dd067eba2','Qibil','0821834583'),
                                                                   ('f67bfae2-d8ce-4d17-85e5-d830a0c32f4e','Nurman','0877567211');

INSERT INTO "public"."m_menu" ("id","name","price") VALUES
                                                        ('83179700-3ee4-46fc-826c-a8a80a8f577f','Nasi Putih',3000),
                                                        ('249fdd63-e12e-48d3-947a-3907c86797a4','Sayur Sop',2000),
                                                        ('a9f1a869-9449-4f7e-849f-b342566679f0','Tahu',2000),
                                                        ('34ca97aa-8604-4fc3-ba01-af0743302d8a','Es Teh Tawar',1500),
                                                        ('8a1b378f-bbd3-4cf9-b1f4-1d5260396001','Sayur Lodeh',2500),
                                                        ('45c7f405-a6d9-4fcb-b34c-114d9e7b7ad8','Tempe',2000),
                                                        ('d5795404-f57d-499a-b516-10593345e36e','Nasi Goreng',12000),
                                                        ('91894e62-bfa9-48ed-9fed-c3f1b5baba6c','Telor Ceplok',5000),
                                                        ('38c02b9b-e367-4e10-a8ee-6310507a1220','Nasi Goreng Spesial',25000),
                                                        ('b7b02405-8bcc-47de-b470-4edb4434a8ad','Sayur Kangkung',1500),
                                                        ('eb2c0e24-af2c-4997-97e7-f5aaba146bbc','Telor Dadar',5000),
                                                        ('9c9c254a-d639-49cf-a33a-0399268bfd31','Kopi Kapal Api',4000),
                                                        ('6e220867-a3a9-4995-a980-eb17c1c84ac6','Aneka Gorengan',2000),
                                                        ('882133e8-eb70-4a35-ad23-c4133087e4fb','Indomie Goreng Telor',10000),
                                                        ('31983cac-53a2-4686-be3e-6975d46e192c','Es Ovaltine',6000),
                                                        ('2eccc934-1c08-4808-8309-8a1f62ef1b9c','Telor Balado',4000),
                                                        ('49ee30af-0974-4c29-8111-f51bf67723eb','Sayur Buncis',3000),
                                                        ('c391f1a8-cf2f-4156-8213-49c68ac96229','Es Teh Manis',1500),
                                                        ('fa3e887d-1274-435f-a7d0-bd8d4d23ecc0','Tempe Orek',3000),
                                                        ('8b4b83c7-ad67-4bb3-a5d7-7e75d40e3bae','Sayur Tahu',3000),
                                                        ('02d97cd6-1f0c-4927-8f34-7662cbe59f85','Indomie Kari Ayam Telor',10000),
--('239313de-9c65-4119-ab31-b20c045817b3','Nasi Putih',4000);

INSERT INTO "public"."m_table" ("id","name") VALUES
                                                 ('ecbcdfd9-a512-410e-ba39-046fe804d86f','T01'),
                                                 ('8a356de0-d860-424d-81ea-70084dc98ecd','T02'),
                                                 ('a7999040-4443-4ac2-b654-a51695b9f8a9','T03'),
                                                 ('cb878ea6-2ee0-4838-9124-9ac287cf023a','T04');

INSERT INTO "public"."m_trans_type" ("id","description") VALUES
                                                             ('DI','Dine In'),
                                                             ('TA','Take Away');

INSERT INTO "public"."t_bill" ("id","trans_date","customer_id","table_id","trans_type") VALUES
                                                                                            ('f9d7c0f5-8951-4eb5-975a-f51bfb7c8af3','2022-06-01','fa77708b-1294-461a-85e4-762268fbdec1','ecbcdfd9-a512-410e-ba39-046fe804d86f','DI'),
                                                                                            ('54f406a6-07f9-4271-9712-36f0ea0cc669','2022-06-01','f67bfae2-d8ce-4d17-85e5-d830a0c32f4e','8a356de0-d860-424d-81ea-70084dc98ecd','DI'),
                                                                                            ('1fec465e-36a6-4309-9c8a-ab112b65934a','2022-06-01','ceb87e94-8667-4d82-a30b-99ad093695e4','ecbcdfd9-a512-410e-ba39-046fe804d86f','DI'),
                                                                                            ('1fca4c18-5c6e-44fe-8bca-13ed869dd162','2022-06-02','a538ec79-b381-4b5c-83ef-049d8972f7fd',NULL,'TA'),
                                                                                            ('a70882e3-cbd4-49b3-961f-0e11e625ae6e','2022-06-03','f67bfae2-d8ce-4d17-85e5-d830a0c32f4e','ecbcdfd9-a512-410e-ba39-046fe804d86f','DI'),
                                                                                            ('973ea7ab-6c3a-4f52-b0d7-b8e96f19091a','2022-06-03','5928c712-bfb8-4614-bd70-bdadf2107535','8a356de0-d860-424d-81ea-70084dc98ecd','DI'),
                                                                                            ('0d0bfebf-d5ea-4306-8fea-9c760343b23d','2022-06-03','46e0c194-2f4b-4e3a-a999-3b6d82302671','a7999040-4443-4ac2-b654-a51695b9f8a9','DI'),
                                                                                            ('e71b7bd9-50bf-428a-baa5-dce04a9df23f','2022-06-03','2bf91858-210b-4d0f-a22d-685dd067eba2','8a356de0-d860-424d-81ea-70084dc98ecd','DI'),
                                                                                            ('ca4989b5-e07f-49aa-b0b3-50da519557cd','2022-06-03','0ebd43b3-4027-4891-80cc-6277fb8caba5','cb878ea6-2ee0-4838-9124-9ac287cf023a','DI'),
                                                                                            ('c4a3a1f7-47ba-4918-a2b1-1b5b36071da2','2022-06-04','f67bfae2-d8ce-4d17-85e5-d830a0c32f4e','ecbcdfd9-a512-410e-ba39-046fe804d86f','DI'),
                                                                                            ('781fa2df-865e-48b9-9cf0-66bfc29f27b9','2022-06-04','5928c712-bfb8-4614-bd70-bdadf2107535','cb878ea6-2ee0-4838-9124-9ac287cf023a','DI'),
                                                                                            ('7b9dfcda-29c9-4a6a-b1e6-80f816de55f9','2022-06-05','5928c712-bfb8-4614-bd70-bdadf2107535',NULL,'TA');

INSERT INTO "public"."t_bill_detail" ("id","bill_id","menu_id","qty","price") VALUES
                                                                                  ('d89338bf-ae54-49e9-9285-39ac986eb8e0','f9d7c0f5-8951-4eb5-975a-f51bfb7c8af3','83179700-3ee4-46fc-826c-a8a80a8f577f',1,3000),
                                                                                  ('81bd2498-f7a8-4874-b64a-23890dffb351','f9d7c0f5-8951-4eb5-975a-f51bfb7c8af3','249fdd63-e12e-48d3-947a-3907c86797a4',1,2000),
                                                                                  ('f027737b-aa53-4476-aa01-3c350c81312a','f9d7c0f5-8951-4eb5-975a-f51bfb7c8af3','a9f1a869-9449-4f7e-849f-b342566679f0',2,2000),
                                                                                  ('0b67a2e2-1b52-4960-80ce-389f2314b72c','f9d7c0f5-8951-4eb5-975a-f51bfb7c8af3','34ca97aa-8604-4fc3-ba01-af0743302d8a',1,1500),
                                                                                  ('10228bac-289d-42ee-9b75-fd4192a2a83f','54f406a6-07f9-4271-9712-36f0ea0cc669','83179700-3ee4-46fc-826c-a8a80a8f577f',1,3000),
                                                                                  ('590afa9e-23c7-47e8-8401-e108b60b6e31','54f406a6-07f9-4271-9712-36f0ea0cc669','8a1b378f-bbd3-4cf9-b1f4-1d5260396001',1,2500),
                                                                                  ('e58dbf60-9c35-4a64-b571-42accd5884e1','54f406a6-07f9-4271-9712-36f0ea0cc669','45c7f405-a6d9-4fcb-b34c-114d9e7b7ad8',2,2000),
                                                                                  ('fe630325-b9a9-433f-97fb-e905803d4239','54f406a6-07f9-4271-9712-36f0ea0cc669','34ca97aa-8604-4fc3-ba01-af0743302d8a',1,1500),
                                                                                  ('44422912-befe-41c3-8447-342855d31e06','1fec465e-36a6-4309-9c8a-ab112b65934a','d5795404-f57d-499a-b516-10593345e36e',1,12000),
                                                                                  ('621c2c4e-ac7b-46f0-b04c-e48744601ef5','1fec465e-36a6-4309-9c8a-ab112b65934a','91894e62-bfa9-48ed-9fed-c3f1b5baba6c',1,5000),
                                                                                  ('dd7d80ed-aae8-493a-840c-0b64f6879166','1fca4c18-5c6e-44fe-8bca-13ed869dd162','38c02b9b-e367-4e10-a8ee-6310507a1220',1,25000),
                                                                                  ('ca16ffb1-8f08-40ef-898e-41f661a05948','a70882e3-cbd4-49b3-961f-0e11e625ae6e','83179700-3ee4-46fc-826c-a8a80a8f577f',1,3000),
                                                                                  ('826bf617-3b64-46a9-824d-e0adc658d01b','a70882e3-cbd4-49b3-961f-0e11e625ae6e','b7b02405-8bcc-47de-b470-4edb4434a8ad',1,1500),
                                                                                  ('75aca36c-fa60-4bc2-b6cc-65877b06b4f4','a70882e3-cbd4-49b3-961f-0e11e625ae6e','eb2c0e24-af2c-4997-97e7-f5aaba146bbc',1,5000),
                                                                                  ('d72a1b29-01e8-4cb6-b546-375c70334e02','a70882e3-cbd4-49b3-961f-0e11e625ae6e','34ca97aa-8604-4fc3-ba01-af0743302d8a',1,1500),
                                                                                  ('3e73ab42-c083-4583-b13d-7c9e284e5c98','973ea7ab-6c3a-4f52-b0d7-b8e96f19091a','9c9c254a-d639-49cf-a33a-0399268bfd31',1,4000),
                                                                                  ('23fc88d6-5c96-458d-9a67-4989727911f1','973ea7ab-6c3a-4f52-b0d7-b8e96f19091a','6e220867-a3a9-4995-a980-eb17c1c84ac6',5,2000),
                                                                                  ('50da9e12-54ce-46ef-9510-b915f532cd65','0d0bfebf-d5ea-4306-8fea-9c760343b23d','882133e8-eb70-4a35-ad23-c4133087e4fb',1,10000),
                                                                                  ('bcda8732-2daf-4019-93fc-ec39ff358451','0d0bfebf-d5ea-4306-8fea-9c760343b23d','31983cac-53a2-4686-be3e-6975d46e192c',1,6000),
                                                                                  ('91ba976b-648b-4c5d-b944-32bb81102e37','e71b7bd9-50bf-428a-baa5-dce04a9df23f','2eccc934-1c08-4808-8309-8a1f62ef1b9c',1,4000),
                                                                                  ('ab175765-c767-4122-b667-67a5ebd10734','e71b7bd9-50bf-428a-baa5-dce04a9df23f','49ee30af-0974-4c29-8111-f51bf67723eb',1,3000),
                                                                                  ('ef9fae63-a2b5-408c-b021-3dca5279fa1c','e71b7bd9-50bf-428a-baa5-dce04a9df23f','6e220867-a3a9-4995-a980-eb17c1c84ac6',2,2000),
                                                                                  ('f5dcabd3-09ff-402e-b66b-923a03cf825e','e71b7bd9-50bf-428a-baa5-dce04a9df23f','c391f1a8-cf2f-4156-8213-49c68ac96229',1,1500),
                                                                                  ('b97952d0-ceef-43cb-9bd6-2d00f2c8cd69','ca4989b5-e07f-49aa-b0b3-50da519557cd','83179700-3ee4-46fc-826c-a8a80a8f577f',1,3000),
                                                                                  ('27e33723-bc82-431b-8caf-4e6228976ad1','ca4989b5-e07f-49aa-b0b3-50da519557cd','fa3e887d-1274-435f-a7d0-bd8d4d23ecc0',1,3000),
                                                                                  ('39b844a9-e113-467f-bc3d-02c2502cb74f','ca4989b5-e07f-49aa-b0b3-50da519557cd','8b4b83c7-ad67-4bb3-a5d7-7e75d40e3bae',1,3000),
                                                                                  ('ebe31559-61e9-4aaf-9123-37270106a80f','ca4989b5-e07f-49aa-b0b3-50da519557cd','34ca97aa-8604-4fc3-ba01-af0743302d8a',1,1500),
--('32da8ac1-6cc0-4748-859c-e865fc63ba3c','c4a3a1f7-47ba-4918-a2b1-1b5b36071da2','239313de-9c65-4119-ab31-b20c045817b3',1,4000),
                                                                                  ('cd89f766-d767-4844-9357-db65f57b2adf','c4a3a1f7-47ba-4918-a2b1-1b5b36071da2','8a1b378f-bbd3-4cf9-b1f4-1d5260396001',1,2500),
                                                                                  ('1c4a70ed-a558-4140-8baf-39af9ee8f889','c4a3a1f7-47ba-4918-a2b1-1b5b36071da2','45c7f405-a6d9-4fcb-b34c-114d9e7b7ad8',1,2000),
                                                                                  ('e87913f5-7262-476e-95a6-550ac929f904','c4a3a1f7-47ba-4918-a2b1-1b5b36071da2','c391f1a8-cf2f-4156-8213-49c68ac96229',1,1500),
                                                                                  ('336d5ee2-5adb-46c4-8127-57a31a7698d3','781fa2df-865e-48b9-9cf0-66bfc29f27b9','02d97cd6-1f0c-4927-8f34-7662cbe59f85',1,10000),
                                                                                  ('1f461fcc-497d-4548-b149-b8f1434aedd4','781fa2df-865e-48b9-9cf0-66bfc29f27b9','c391f1a8-cf2f-4156-8213-49c68ac96229',1,1500),
                                                                                  ('ce075057-7a7f-423d-94e5-814c4e0df179','7b9dfcda-29c9-4a6a-b1e6-80f816de55f9','6e220867-a3a9-4995-a980-eb17c1c84ac6',15,2000),
                                                                                  ('5f7e8a71-0edf-409f-b5d7-a0b7ada3f846','e71b7bd9-50bf-428a-baa5-dce04a9df23f','83179700-3ee4-46fc-826c-a8a80a8f577f',0.5,3000);


#define SIZE 500
#include <stdio.h>
#include <stdlib.h>
#include <libpq-fe.h>
#include <string.h>

/* Steps */
/* 1. Kanw egkatastash to postgresql */
/* 2. Kanw egkatastash to pgAdmin3 gia na mporw na diaxeiristw thn vash */
/* 3. Ftiaxnw ena neo xrhsth sthn postgres : sudo -u postgres createuser left */
/* 4. Ama exei thema me to peer paw sto config file ths postgres kai to allazw se md5 apo peer 
    https://gist.github.com/AtulKsol/4470d377b448e56468baef85af7fd614
*/

// 0 : SELECT
// 1: INSERT
PGresult * executeQuery(char *queryStr,PGconn * connection,int queryType){
	
	PGresult *res;
	
	if(queryType==0){
		res=PQexec(connection,queryStr);
		if (PQresultStatus(res) != PGRES_TUPLES_OK){
	        printf("Query command failed: %s", PQerrorMessage(connection));
	        PQclear(res);
	    }	
	}
	else{
		res=PQexec(connection,queryStr);
		if (PQresultStatus(res) != PGRES_COMMAND_OK){
	        printf("Query command failed: %s", PQerrorMessage(connection));
	        PQclear(res);
	    }	
	}
    return res;
}

int main(int argc,char **argv){
	PGconn* connection = PQconnectdb("user=postgres password=postgres dbname=eidiko_thema port=5433");
	PGresult *res;
	
	if(PQstatus(connection)!=CONNECTION_OK){
    	printf("Error at connection!: %s\n",PQerrorMessage(connection));
    	exit(1);
	}
	printf("Connected Successfully!\n");
    	
    char *queryStr=(char *)malloc(sizeof(char)*SIZE);
	if(queryStr==NULL){
		printf("Malloc allocation failed.\n");
		exit(1);
	}	
	
    memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADMIN(password,username,admin_id) VALUES('12345','jack',1)");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADMIN(password,username,admin_id) VALUES('aaadd','nick',2)");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADMIN(password,username,admin_id) VALUES('zxcvbnm','joe',3)");
	res=executeQuery(queryStr,connection,1);
	
	printf("Admins added successfully\n");
	

	//------------------------------------------------- Αddresses ------------------------------------------------------------//
    memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADDRESS(address_id,address_name,address_number,address_zipcode,address_city,address_country) VALUES(1,'Ronda de Toledo',26,28966,'Madrid','Spain')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADDRESS(address_id,address_name,address_number,address_zipcode,address_city,address_country) VALUES(2,'Crawford St',58,16500,'London','England')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADDRESS(address_id,address_name,address_number,address_zipcode,address_city,address_country) VALUES(3,'Breite Str',116,48770,'Berlin','Germany')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADDRESS(address_id,address_name,address_number,address_zipcode,address_city,address_country) VALUES(4,'Brill St',8,12000,'New York','USA')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADDRESS(address_id,address_name,address_number,address_zipcode,address_city,address_country) VALUES(5,'Oak Avenue',15,60020,'Fox Lake','USA')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADDRESS(address_id,address_name,address_number,address_zipcode,address_city,address_country) VALUES(6,'Dovetail Estates',26,736001,'Clinton','USA')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADDRESS(address_id,address_name,address_number,address_zipcode,address_city,address_country) VALUES(7,'Tenmile Road',34,02137,'Cambridge','USA')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADDRESS(address_id,address_name,address_number,address_zipcode,address_city,address_country) VALUES(8,'Privada',122,54123,'Havana','Cuba')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADDRESS(address_id,address_name,address_number,address_zipcode,address_city,address_country) VALUES(9,'Chofu',345,21495,'Tokyo','Japan')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADDRESS(address_id,address_name,address_number,address_zipcode,address_city,address_country) VALUES(10,'Los Poblados',24,18956,'Madrid','Spain')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADDRESS(address_id,address_name,address_number,address_zipcode,address_city,address_country) VALUES(11,'Al Sokari',112,45895,'Cairo','Egypt')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADDRESS(address_id,address_name,address_number,address_zipcode,address_city,address_country) VALUES(12,'Beira Rd',58,18956,'Colombo','Sri Lanka')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADDRESS(address_id,address_name,address_number,address_zipcode,address_city,address_country) VALUES(13,'Xicheng',64,28468,'Beijing','China')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO ADDRESS(address_id,address_name,address_number,address_zipcode,address_city,address_country) VALUES(14,'Wolgok',14,69562,'Gwangju','Korea')");
	res=executeQuery(queryStr,connection,1);
	//------------------------------------------------- Αddresses ------------------------------------------------------------//

	printf("Addresses added successfully\n");	
	
	//----------------------------------------------------- Customers --------------------------------------------------------------------//
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('1234','john','johnpatel@gmail.com','Patel','John',1,'6936957634',1)");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('1234qwe','niko','nikobellic@gmail.com','Bellic','Niko',2,'2025550191',4)");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('zxcvbnm','simon','simoncarita@gmail.com','Simon','Carita',3,'6281248970',2)");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('zxcvbnm','raul','raulcarita@gmail.com','Raul','Carita',4,'6286348976',2)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('45asdw','lferrell','lferrell@gmail.com','Leopold','Ferrell',5,'1254826475',3)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('dqwe45','hbryan','hbryan@gmail.com','Harper','Bryan',6,'5469513758',4)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('fsq956','annie','annie@gmail.com','Anne-Marie','Thomas',7,'54679424561',5)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('fweq887','delliot','delliot@gmail.com','Daisie','Elliot',8,'9568251645',6)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('45aswe','lyonl','lyonl@gmail.com','Leroy','Lyon',9,'1569598745',6)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('5asd4qw','npayne','npayne@gmail.com','Nicola','Payne',10,'65845878995',7)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('1234','zpater','zpater@gmail.com','Zoe','Paterson',11,'42846956237',8)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('1234','keaston','keaston@gmail.com','Konrad','Easton',12,'5687426595',9)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('4asdwq','egreig','egreig@gmail.com','Efa','Greig',13,'84975625156',10)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('eqss887','alkim','alkim@gmail.com','Alister','Kim',14,'8469523688',11)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('dqwe84','thale','thale@gmail.com','Tesa','Hale',15,'236955458712',12)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('kless12','fhuerta','fhuerta@gmail.com','Finley','Huerta',16,'4588695613',13)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone,fk1_address_id) \
	VALUES('1234','cbishop','cbishop@gmail.com','Chad','Bishop',17,'2468456225',14)");
	res=executeQuery(queryStr,connection,1);
	

	//----------------------------------------------------- Customers --------------------------------------------------------------------//

	printf("Customers added successfully\n");
	
    memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO SUPPLIER(supplier_name,supplier_phone) VALUES('Microsoft','6934567321')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO SUPPLIER(supplier_name,supplier_phone) VALUES('Nintendo','2103452378')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO SUPPLIER(supplier_name,supplier_phone) VALUES('AmazonBasics','2132456789')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO SUPPLIER(supplier_name,supplier_phone) VALUES('Fujitsu','7687453490')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO SUPPLIER(supplier_name,supplier_phone) VALUES('SanDisk','1234578969')");
	res=executeQuery(queryStr,connection,1);
	
    printf("Suppliers added successfully\n");
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO PRODUCT(product_id,product_price,product_desc,product_name,fk1_supplier_name) VALUES(1,59.99,'Compatible with Xbox one , windows 10','Xbox Controller','Microsoft')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO PRODUCT(product_id,product_price,product_desc,product_name,fk1_supplier_name) VALUES(2,300,'Nintedo switch console','Nintendo Switch','Nintendo')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO PRODUCT(product_id,product_price,product_desc,product_name,fk1_supplier_name) VALUES(3,6.99,'Cable feet : 6 (1.8 meters)','HDMI Cable','AmazonBasics')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO PRODUCT(product_id,product_price,product_desc,product_name,fk1_supplier_name) VALUES(4,419.99,'Compatible with ios and Windows devices','ScanSnap iX500','Fujitsu')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO PRODUCT(product_id,product_price,product_desc,product_name,fk1_supplier_name) VALUES(5,10.19,'Great for cameras','SanDisk 32GB','SanDisk')");
	res=executeQuery(queryStr,connection,1);
	
	printf("Products added successfully\n");
	

	//------------------------------------------------------------ Orders -----------------------------------------------------------------//
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(1,'Credit card',1,1,'No comments',5)");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(2,'Paysafe',2,2,'No comments',1)");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(3,'Paysafe',2,1,'No comments',5)");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(4,'Paypal',3,5,'No comments',2)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(5,'Credit Card',3,1,'No comments',2)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(6,'Paysafe',4,2,'No comments',3)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(7,'Credit Card',4,1,'No comments',1)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(8,'Paysafe',5,5,'No comments',5)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(9,'Paysafe',5,1,'No comments',3)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(10,'Paypal',6,3,'No comments',2)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(11,'Credit Card',6,4,'No comments',2)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(12,'Paypal',7,3,'No comments',1)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(13,'Paysafe',8,4,'No comments',1)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(14,'Credit Card',9,1,'No comments',1)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(15,'Cash',10,1,'No comments',10)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(16,'Cash',10,2,'No comments',3)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(17,'Cash',10,3,'No comments',4)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(18,'Cash',10,4,'No comments',10)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(19,'Cash',10,5,'No comments',3)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(20,'Cash',11,5,'No comments',2)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(21,'Paysafe',11,1,'No comments',4)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(22,'Paysafe',12,2,'No comments',4)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(23,'Credit Card',12,1,'No comments',5)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(24,'Paypal',13,5,'No comments',2)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(25,'Paysafe',14,2,'No comments',3)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(26,'Credit Card',15,5,'No comments',1)");
	res=executeQuery(queryStr,connection,1);
    
    memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(27,'Credit Card',16,2,'No comments',2)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(28,'Credit Card',17,2,'No comments',2)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(29,'Paypal',15,1,'No comments',1)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(30,'Paysafe',16,2,'No comments',2)");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO \"Order\"(order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) VALUES(31,'Credit Card',13,1,'No comments',2)");
	res=executeQuery(queryStr,connection,1);
	//------------------------------------------------------------ Orders -----------------------------------------------------------------//

    printf("Orders added successfully\n");
    
	//------------------------------------------------------------ Orders -----------------------------------------------------------------//

    memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(1,DATE '2018-10-20',1,'Arrival in 2 days')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(2,DATE '2018-10-19',2,'Arrival in 4 days')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(3,DATE '2018-10-19',3,'Arrival in 4 days')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(4,DATE '2018-10-17',4,'Arrival in 1 day')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(5,DATE '2018-10-18',5,'Arrival in 1 day')");
	res=executeQuery(queryStr,connection,1);
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(6,DATE '2018-10-20',6,'Arrival in 2 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(7,DATE '2018-10-18',7,'Arrival in 4 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(8,DATE '2018-10-19',8,'Arrival in 4 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(9,DATE '2018-10-18',9,'Arrival in 3 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(10,DATE '2018-10-20',10,'Arrival in 1 day')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(11,DATE '2018-10-17',11,'Arrival in 1 day')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(12,DATE '2018-10-18',12,'Arrival in 2 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(13,DATE '2018-10-19',13,'Arrival in 5 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(14,DATE '2018-10-19',14,'Arrival in 2 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(15,DATE '2018-10-17',15,'Arrival in 1 day')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(16,DATE '2018-10-17',16,'Arrival in 1 day')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(17,DATE '2018-10-20',17,'Arrival in 2 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(18,DATE '2018-10-18',18,'Arrival in 3 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(19,DATE '2018-10-16',19,'Arrival in 2 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(20,DATE '2018-10-20',20,'Arrival in 2 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(21,DATE '2018-10-20',21,'Arrival in 1 day')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(22,DATE '2018-10-20',22,'Arrival in 3 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(23,DATE '2018-10-18',23,'Arrival in 4 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(24,DATE '2018-10-18',24,'Arrival in 4 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(25,DATE '2018-10-17',25,'Arrival in 1 day')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(26,DATE '2018-10-18',26,'Arrival in 4 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(27,DATE '2018-10-19',27,'Arrival in 2 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(28,DATE '2018-10-17',28,'Arrival in 2 days')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(29,DATE '2018-10-17',29,'Arrival in 1 day')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(30,DATE '2018-10-20',30,'Arrival in 1 day')");
	res=executeQuery(queryStr,connection,1);

	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(31,DATE '2018-10-19',31,'Arrival in 3 days')");
	res=executeQuery(queryStr,connection,1);


	//------------------------------------------------------------ Orders -----------------------------------------------------------------//
	
	printf("Deliveries added successfully\n");
    
    PQfinish(connection);
	return 0;
}


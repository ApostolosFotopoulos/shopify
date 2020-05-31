#define SIZE 500
#include <stdio.h>
#include <stdlib.h>
#include <libpq-fe.h>
#include <string.h>
#include <time.h>
#include <ctype.h>

enum customerActions{MAKE_ORDER=1,SHOW_PRODUCTS,SHOW_ORDERS,TOTAL_COST,CANCEL_ORDER,DELETE_ACCOUNT,LOGOUT};
enum userActions{LOGIN_ADMIN=1,LOGIN_CUSTOMER,NEW_ADMIN,NEW_CUSTOMER,EXIT};
enum adminActions{ADD_PRODUCT=1,SHOW_ALL_ORDERS,EDIT_DELIVERY,BEST_CUSTOMERS,BEST_CITIES,DELETE_ADMIN_ACCOUNT,ADMIN_LOGOUT};

//Using fgets to get input
void readStr(char *buf){
	//Flush 
	fflush(stdin);
	if (!fgets(buf, SIZE, stdin)) {
		printf("Error using fgets\n");	
		exit(1);
	}
	else{
		buf[strlen(buf)-1]='\0';
	}
}

//Checks if the input is an integer
int checkInput(){
	
	//Flush 
	fflush(stdin);
	
	int temp;
	
	char *end;
	char buf[SIZE];
	
	if (!fgets(buf, sizeof buf, stdin)) {
		return -1;
	}
	        
    // remove \n
    buf[strlen(buf) - 1] = 0;
	
	temp = strtol(buf, &end, 10);
	
	if ((end != buf + strlen(buf)) )  {
		return -1;
	}
	return temp;
}

// 0 : SELECT
// 1: INSERT
//Executes a query and checks for errors based on its type
PGresult * executeQuery(char *queryStr,PGconn * connection,int queryType){
	
	PGresult *res;
	if(queryType==0){
		res=PQexec(connection,queryStr);
		if (PQresultStatus(res) != PGRES_TUPLES_OK){
	        	printf("Query command failed: %s\n", PQerrorMessage(connection));
	        	PQclear(res);
	    	}	
	}
	else{
		res=PQexec(connection,queryStr);
		if (PQresultStatus(res) != PGRES_COMMAND_OK){
	        	printf("Query command failed: %s\n", PQerrorMessage(connection));
	        	PQclear(res);
	    	}	
	}
    return res;
}

//Shows all the products and its characteristics
void  showProducts(char *queryStr,PGconn *connection){
	// Show all products
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"SELECT * FROM PRODUCT");
	PGresult *res=executeQuery(queryStr,connection,0);
	
	int product_id__col = PQfnumber(res, "product_id");
	int product_price_col = PQfnumber(res, "product_price");
	int product_desc_col = PQfnumber(res, "product_desc");
	int product_name_col = PQfnumber(res, "product_name");
	int i;
	for(i=0;i<PQntuples(res);i++){
		printf("---------------------------------------------------------\n");
		printf("Product id: %s\n",PQgetvalue(res, i, product_id__col));
		printf("Name : %s\n",PQgetvalue(res, i, product_name_col));
		printf("Price: %s$\n",PQgetvalue(res, i, product_price_col));
		printf("Description: %s\n",PQgetvalue(res, i, product_desc_col));
		printf("---------------------------------------------------------\n");
	}
	//Clean memory
	PQclear(res);
}

//Shows all orders made by customers
void showOrders(char *queryStr,PGconn *connection,int customer_id){
	
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"SELECT * FROM \"Order\" O, PRODUCT P where O.fk1_customer_id =%d and P.product_id =O.fk2_product_id",customer_id);
	PGresult *res=executeQuery(queryStr,connection,0);
	
	
	if(PQntuples(res) >0){
		int order_id_col = PQfnumber(res, "order_id");
		int payment_method_col = PQfnumber(res, "payment_method");
		int price_col = PQfnumber(res, "product_price");
		int comments_col = PQfnumber(res, "comments");
		int quantity_col =PQfnumber(res, "product_quantity_per_product");
		int product_name_col =PQfnumber(res, "product_name");
		int i;
		for(i=0;i<PQntuples(res);i++){
	
			memset(queryStr,'\0',SIZE); 
			sprintf(queryStr,"SELECT status,departure_date FROM Delivery D where D.fk1_order_id =%d",atoi(PQgetvalue(res, i, order_id_col)) );
			PGresult *getDelivery=executeQuery(queryStr,connection,0);
			
			int status_col=PQfnumber(getDelivery, "status");
			int dep_col=PQfnumber(getDelivery, "departure_date");
			
			printf("---------------------------------------------------------\n");
			printf("Order id: %s\n",PQgetvalue(res, i, order_id_col));
			printf("Payment Method : %s\n",PQgetvalue(res, i, payment_method_col));
			printf("Product Name: %s\n",PQgetvalue(res, i, product_name_col));
			printf("Price: %s $\n",PQgetvalue(res, i, price_col));
			printf("Quantity: %s\n",PQgetvalue(res, i, quantity_col));
			printf("Status: %s\n",PQgetvalue(getDelivery, 0, status_col));
			printf("Departure Date: %s\n",PQgetvalue(getDelivery, 0, dep_col));
			printf("Comments: %s\n",PQgetvalue(res, i, comments_col));
			printf("---------------------------------------------------------\n");
			PQclear(getDelivery);
		}
	
	}
	else{
		printf("No Orders Made.\n");
	}

	//clean memory
	PQclear(res);
}

//Calculates the total cost for a customer
void calculate_cost(char *queryStr,PGconn *connection,int customer_id){
	memset(queryStr,'\0',SIZE); 
	sprintf(queryStr,"SELECT SUM(product_price * product_quantity_per_product) FROM PRODUCT P,\"Order\" O	\
	WHERE P.product_id = O.fk2_product_id and O.fk1_customer_id=%d",customer_id);
	PGresult *res=executeQuery(queryStr,connection,0);
	
	double sum = atof(PQgetvalue(res,0,0));
	printf("Sum: %.3lf\n",sum);
	
	PQclear(res);
}

// Prints the menu and lets the user choose 
int chooseOption(){

	//Flush stdin
	fflush(stdin);
	
	int option=-1;
	
	while(1){
		printf("\nOptions : \n1.Login Admin\n2.Login Customer\n3.New Admin\n4.New Customer\n5.Exit\n\n");
		printf("Choose: ");
		
		option=checkInput();
		if((option<LOGIN_ADMIN || option>EXIT)){
			printf("Invalid input\n");
		}
		else{
			break;
		}
	}
		
	return option;
}

//Prints the customer menu
int  customerMenu(){
	
	int action=-1;			
	
	//Flush stdin
	fflush(stdin);
	
	while(1){
		printf("\nActions: \n1.Make an order \n2.Show Products\n3.Show Orders\n4.Total Cost\n5.Cancel order\n6.Delete Account\n7.Logout\n\n");
		printf("Choose: ");
		
		action=checkInput();
		if(  action < MAKE_ORDER || action > LOGOUT){
			printf("Invalid input\n");
		}
		else{
			break;
		}
	}
	return action;
}

//Prints the admin menu
int adminMenu(){
	int action=-1;			
	
	//Flush stdin
	fflush(stdin);
	
	while(1){
		printf("\nActions: \n1.Add Product\n2.Show all orders\n3.Edit Delivery Status\n4.Show top 10 current customers\n5.Show top 10 cities\n6.Delete Admin\n7.Logout\n\n");
		printf("Choose: ");
		
		action=checkInput();
		if(  action < ADD_PRODUCT || action > ADMIN_LOGOUT){
			printf("Invalid input\n");
		}
		else{
			break;
		}
	}
	return action;
}

// Get the input data for user / admin 
void getInput(char *username,char * password){
	
	printf("Enter username: ");
	readStr(username);
	printf("Enter password: ");
	readStr(password);
}

// Insert new username 
void insertUsername(char *queryStr,char *username,PGconn *connection,char *password,char *type){
	
	PGresult  *res;
	do{
		// Get the data from the user 
		getInput(username,password);	
		
		// Search if the username we entered already exists 
		sprintf(queryStr,"SELECT username FROM %s A where A.username = '%s'",type,username);
		res=executeQuery(queryStr,connection,0);
		
		
	    if(PQntuples(res)==1){
	    	printf("Username already exists!!\n");
		}
	}while(PQntuples(res)==1);	
	PQclear(res);
}

//Checks if the authentication credentials matches
int checkForUser(char *queryStr,char *username,PGconn *connection,char *password,char *type){
	
	PGresult  *res;
	// Get the data from the user 
	getInput(username,password);	
	
	// Search if the username we entered already exists 
	sprintf(queryStr,"SELECT username,password FROM %s A where A.username = '%s'",type,username);
	res=executeQuery(queryStr,connection,0);	
    
   	 if(PQntuples(res)==1){
    		int password_col = PQfnumber(res, "password");
	
		if (strcmp(password,PQgetvalue(res, 0, password_col))==0) {
			printf("Login was successful\n");
			return 1;
		}
		else {
			printf("Wrong password\n");
		}
	}
	else {
		printf("Invalid username\n");
	}
	return 0;
}

//Checks for any malloc errors
void mallocCheck(char *buf){
	if(buf==NULL){
		printf("Malloc allocation failed.\n");
		exit(1);
	}
	memset(buf,'\0',SIZE);
}

int main(int argc,char **argv){

	PGconn *connection;
	PGresult *res=NULL;
	
	// --------------------------------------------------Initialize the memory for inputs -----------------------------------------------------------------------------------
	char *username=(char *)malloc(sizeof(char) *SIZE);
	mallocCheck(username);
	
	char *password=(char *)malloc(sizeof(char)*SIZE);
	mallocCheck(password);
	
	// Format string for query 
	char *queryStr=(char *)malloc(sizeof(char)*SIZE);
	mallocCheck(queryStr);
	
	// ---------------------------------------------- Initialize the memory for inputs ----------------------------------------------------------------------------------------

	// ----------------------------------------------- Connect to database -----------------------------------------------------------------------------------------------------
		
	connection = PQconnectdb("user=postgres password=postgres dbname=eidiko_thema port=5432");

	if(PQstatus(connection)!=CONNECTION_OK){
    		printf("Error at connection!: %s\n",PQerrorMessage(connection));
    		exit(1);
	}
	printf("Connected Successfully!\n");
	
	// ----------------------------------------------- Connect to database -------------------------------------------------------------------------------------------------------

	while(1){
		
		//Flush
		fflush(stdin);
		
		//Clear the memory
		memset(username,'\0',SIZE);
		memset(password,'\0',SIZE);
		memset(queryStr,'\0',SIZE);
		
		// Choose option from the menu 
		int opt=chooseOption();
		
		// ----------------------------------------------- Admin Login -------------------------------------------------------------------------------------------------------------
		if(opt==LOGIN_ADMIN){
			
			if(checkForUser(queryStr,username,connection,password,"ADMIN")){
				memset(queryStr,'\0',SIZE); 
				sprintf(queryStr,"SELECT admin_id FROM ADMIN A where A.username='%s'",username);
				res=executeQuery(queryStr,connection,0);
				
				int admin_id=atoi(PQgetvalue(res,0,0));
				
				//Clear memory
				PQclear(res);			
		
				while(1){
				
					int action = adminMenu();
					
					// ----------------------------------------------- Admin Adds a Product -------------------------------------------------------------------------------------------------------------
					if(action==ADD_PRODUCT){
						
						char *product_desc = (char *)malloc(sizeof(char)*SIZE);
						mallocCheck(product_desc);
						
						char *product_name = (char *)malloc(sizeof(char)*SIZE);
						mallocCheck(product_name);
						
						char * supplier_name = (char *)malloc(sizeof(char)*SIZE);
						mallocCheck(supplier_name);
						
						char * supplier_phone = (char *)malloc(sizeof(char)*SIZE);
						mallocCheck(supplier_phone);
						
						
						printf("Enter product name: ");
						readStr(product_name);
						
						memset(queryStr,'\0',SIZE);	
						sprintf(queryStr,"SELECT product_id FROM Product P where P.product_name='%s'",product_name);
						res=executeQuery(queryStr,connection,0);
						
						if(PQntuples(res)==0){
							
							printf("Enter product description: ");
							readStr(product_desc);
							
							printf("Enter supplier: ");
							readStr(supplier_name);		
							
							//Clean memory 
							PQclear(res);
	
							memset(queryStr,'\0',SIZE);	
							sprintf(queryStr,"SELECT supplier_name FROM SUPPLIER S where S.supplier_name='%s'",supplier_name);
							res=executeQuery(queryStr,connection,0);
							
							if(PQntuples(res)==1){
								
								//Clean memory
								PQclear(res);
	
								double price=0;
							
								//Read the price
								while(price==0){
									printf("Enter price: ");	
									price=checkInput();
									printf("Price: %.3lf\n",price);
									
									if(price<=0){
										printf("Invalid input\n");
										//Flush
										fflush(stdin);
									}
								}
								
		
								//Get the new product id
								memset(queryStr,'\0',SIZE);	
								sprintf(queryStr,"SELECT max(product_id) FROM Product ");
								res=executeQuery(queryStr,connection,0);
				
								// New id of the new product
								int product_id=atoi(PQgetvalue(res,0,0)) +1;
								
								//Clean memory 
								PQclear(res);
	
								memset(queryStr,'\0',SIZE);	
								sprintf(queryStr,"INSERT INTO PRODUCT(product_id,product_price,product_desc,product_name,fk1_supplier_name) \
								VALUES(%d,%lf,'%s','%s','%s')",product_id,price,product_desc,product_name,supplier_name);
								res=executeQuery(queryStr,connection,1);
								
								//Clean memory
								PQclear(res);
							}
							else{
								
								printf("Enter supplier phone: ");
								readStr(supplier_phone);
								
								//Clean memory
								PQclear(res);
			
								//Insert the supplier
								memset(queryStr,'\0',SIZE);	
								sprintf(queryStr,"INSERT INTO SUPPLIER(supplier_name,supplier_phone) \
								VALUES('%s','%s')",supplier_name,supplier_phone);
								res=executeQuery(queryStr,connection,1);
								
								//Clean memory
								PQclear(res);
								
								double price=0;
							
								//Read the price
								while(price==0){
									printf("Enter price: ");	
									price=checkInput();
									printf("Price: %.3lf\n",price);
									
									if(price<=0){
										printf("Invalid input\n");
										//Flush
										fflush(stdin);
									}
								}
								
								//Get the new product id
								memset(queryStr,'\0',SIZE);	
								sprintf(queryStr,"SELECT max(product_id) FROM Product ");
								res=executeQuery(queryStr,connection,0);
				
								// New id of the new product
								int product_id=atoi(PQgetvalue(res,0,0)) +1;
								
								//Clean memory
								PQclear(res);
								
								memset(queryStr,'\0',SIZE);	
								sprintf(queryStr,"INSERT INTO PRODUCT(product_id,product_price,product_desc,product_name,fk1_supplier_name) \
								VALUES(%d,%lf,'%s','%s','%s')",product_id,price,product_desc,product_name,supplier_name);
								res=executeQuery(queryStr,connection,1);
								
								//Clean memory
								PQclear(res);
							}
							
						}
						else{
							printf("Product Already exists!\n");
							PQclear(res);
						}
						
						//Clear memory
						free(product_name);
						free(supplier_name);
						free(product_desc);
						free(supplier_phone);					
					}
					// ----------------------------------------------- Show all orders -------------------------------------------------------------------------------------------------------------
					else if(action ==SHOW_ALL_ORDERS){
						
						//Clean memory
						//PQclear(res);
	
						memset(queryStr,'\0',SIZE);	
						sprintf(queryStr,"SELECT customer_id,customer_fname,customer_lname,status,delivery_id,product_name FROM CUSTOMER C, \"Order\" O,DELIVERY D ,Product P \
						where C.customer_id = O.fk1_customer_id and O.order_id =D.fk1_order_id and O.fk2_product_id =P.product_id ORDER BY(customer_id)");
						res=executeQuery(queryStr,connection,0);
						
						if(PQntuples(res)>0){
							
							int customer_fname_col =PQfnumber(res,"customer_fname");
							int customer_lname_col =PQfnumber(res,"customer_lname");
							int	status_col=PQfnumber(res,"status");
							int product_name_col=PQfnumber(res,"product_name");
							int delivery_id_col=PQfnumber(res,"delivery_id");
							
							int i;
							for(i=0;i<PQntuples(res);i++){
								printf("---------------------------------------------------------\n");
								printf("Firstname: %s\n",PQgetvalue(res,i,customer_fname_col));
								printf("Lastname: %s\n",PQgetvalue(res,i,customer_lname_col));
								printf("Product name: %s\n",PQgetvalue(res,i,product_name_col));
								printf("Delivery id: %s\n",PQgetvalue(res,i,delivery_id_col));
								printf("Status: %s\n",PQgetvalue(res,i,status_col));
								printf("---------------------------------------------------------\n");
							} 
						}
						else{
							printf("No orders have been made.\n");
						}
						//Clean memory
						PQclear(res);
					}
					// ----------------------------------------------- Edit delivery status -------------------------------------------------------------------------------------------------------------
					else if(action == EDIT_DELIVERY){
						
						printf("Enter delivery id: ");
						int delivery_id=checkInput();
	
						//Get the new product id
						memset(queryStr,'\0',SIZE);	
						sprintf(queryStr,"SELECT delivery_id from DELIVERY D where D.delivery_id=%d",delivery_id);
						res=executeQuery(queryStr,connection,0);
						
						if(PQntuples(res)==1){
							
							char *status =(char *)malloc(sizeof(char)*SIZE);
							mallocCheck(status);
							
							printf("Enter new status: ");
							readStr(status);
							
							//Clean memory
							PQclear(res);
							//Get the new product id
							memset(queryStr,'\0',SIZE);	
							sprintf(queryStr,"UPDATE DELIVERY D SET status='%s' where D.delivery_id=%d",status,delivery_id);
							res=executeQuery(queryStr,connection,1);
							
							printf("Delivery has been successfully updated.\n");
							
							//Clean memory
							free(status);
						}
						else{
							printf("No orders have been made.\n");
						}
	
						//Clean memory
						PQclear(res);
					}
					// ----------------------------------------------- Show top 10 customers -------------------------------------------------------------------------------------------------------------
					else if( action ==BEST_CUSTOMERS){
						memset(queryStr,'\0',SIZE);	
						sprintf(queryStr,"SELECT customer_fname,customer_lname,customer_id, SUM(P.product_price * O.product_quantity_per_product) AS SUM_RESULT \
						FROM CUSTOMER C,PRODUCT P,\"Order\" O \
						WHERE O.fk1_customer_id = C.customer_id and O.fk2_product_id=P.product_id \
	    				GROUP BY (customer_fname,customer_lname,customer_id) \
						ORDER BY SUM_RESULT DESC \
						LIMIT 10");
						res=executeQuery(queryStr,connection,0);
						
						if(PQntuples(res)>0){
							int customer_fname_col=PQfnumber(res,"customer_fname");
							int customer_lname_col=PQfnumber(res,"customer_lname");
							int customer_id_col=PQfnumber(res,"customer_id");
							
							int i;
							for(i=0;i<PQntuples(res);i++){
								printf("---------------------------------------------------------\n");
								printf("Position: %d\n",i+1);
								printf("Customer name: %s\n",PQgetvalue(res,i,customer_fname_col));
								printf("Customer lastname: %s\n",PQgetvalue(res,i,customer_lname_col));
								int customer_id=atoi(PQgetvalue(res,i,customer_id_col));
								calculate_cost(queryStr,connection,customer_id);
								printf("---------------------------------------------------------\n");
							}
						}
						else{
							printf("No customers available.\n");
						}
						//Clean memory
						PQclear(res);
					}
					// ----------------------------------------------- Show Top 10 Cities -------------------------------------------------------------------------------------------------------------
					else if(action == BEST_CITIES){

						memset(queryStr,'\0',SIZE); 
						sprintf(queryStr,"SELECT address_city,COUNT(*) AS Count  \
						FROM CUSTOMER C,ADDRESS A \
						WHERE C.fk1_address_id =A.address_id \
						GROUP BY A.address_city \
						ORDER BY Count DESC \
						LIMIT 10");
						res=executeQuery(queryStr,connection,0);
						
						int address_city_col=PQfnumber(res,"address_city");
						int count_col=PQfnumber(res,"count");
						
						char *tempStr =(char *)malloc(sizeof(char)*SIZE);
						mallocCheck(tempStr);
						memset(tempStr,'\0',SIZE);
						
						int i;
						printf("---------------------------------------------------------\n");
						printf("City | Number of customers\n");
						for(i=0;i<PQntuples(res);i++){				
							
							int j;
							for(j=0;j<strlen(PQgetvalue(res,i,address_city_col));j++){
								
								if(PQgetvalue(res,i,address_city_col)[j] != ' '){
									tempStr[j]=PQgetvalue(res,i,address_city_col)[j];
								}
								else if(j+1 <strlen(PQgetvalue(res,i,address_city_col)) && isalpha(PQgetvalue(res,i,address_city_col)[j+1])){
									tempStr[j]=' ';
								}
							}
							printf("%s -> %s\n",tempStr,PQgetvalue(res,i,count_col));
							memset(tempStr,'\0',SIZE);
						}
						printf("---------------------------------------------------------\n");
						
						//Clean memory 
						PQclear(res);
						free(tempStr);
					}
					// ----------------------------------------------- Delete Admin Account -------------------------------------------------------------------------------------------------------------
					else if(action == DELETE_ADMIN_ACCOUNT){
						char choice='1';
						
						while(choice!='y' && choice!='Y' && choice!='n' && choice!='N'){
							printf("Are you sure you want to delete your account(y,n): ");
							scanf("%c",&choice);
						}
						
						if(choice=='y' || choice=='Y'){
							//Delete the user
							memset(queryStr,'\0',SIZE); 
							sprintf(queryStr,"DELETE FROM ADMIN A where A.admin_id=%d",admin_id);
							res=executeQuery(queryStr,connection,1);	
							
							//Auto logout
							printf("Account has been deleted successfully.");
							
							//Clean memory
							PQclear(res);
							break;		
						}
					}
					// ----------------------------------------------- Admin Logout -------------------------------------------------------------------------------------------------------------
					else if (action == ADMIN_LOGOUT){
						break;
					}
				}
			}
		}
		// ----------------------------------------------- Customer Login -------------------------------------------------------------------------------------------------------------
		else if(opt==LOGIN_CUSTOMER){
			
			if(checkForUser(queryStr,username,connection,password,"CUSTOMER")){
			
				memset(queryStr,'\0',SIZE); 
				sprintf(queryStr,"SELECT customer_id FROM CUSTOMER C where C.username='%s'",username);
				res=executeQuery(queryStr,connection,0);
				
				int customer_id=atoi(PQgetvalue(res,0,0));
				//printf("Customer id: %d\n",customer_id);
				
				//Clean memory
				PQclear(res);
	
				while(1){
							
					//Get the choice
					int action=customerMenu();
						
					// ----------------------------------------------- Customer makes order -------------------------------------------------------------------------------------------------------------
					if(action==MAKE_ORDER){
						
						while(1){
							int product_id;
							printf("Enter product id: ");
							
							product_id=checkInput();
							if(product_id<=0){
								printf("Invalid input\n");
							}
							else {
								//Check if exists
								memset(queryStr,'\0',SIZE); 
								sprintf(queryStr,"SELECT product_id FROM PRODUCT P where P.product_id = %d",product_id);
								res=executeQuery(queryStr,connection,0);
								
								if(PQntuples(res)==1){
									
									while(1){
										int product_quantity;
										printf("Enter product quantity: ");
										
										product_quantity=checkInput();
										
										if(product_quantity<=0){
											printf("Invalid input\n");
										}
										else{
											char *payment_method= (char *)malloc(sizeof(char)*SIZE);
											mallocCheck(payment_method);
											char *comments= (char *)malloc(sizeof(char)*SIZE);
											mallocCheck(comments);
											
											printf("Enter payment method: ");
											readStr(payment_method);
											
											printf("Enter comments: ");
											readStr(comments);
											
											//Clean memory 
											PQclear(res);
		
	
											//Count the orders 
											memset(queryStr,'\0',SIZE);	
											sprintf(queryStr,"SELECT max(order_id) FROM \"Order\" ");
											res=executeQuery(queryStr,connection,0);
	
											// New id of the new user
											int order_id=atoi(PQgetvalue(res,0,0)) +1;
											
											//Clean memory
											PQclear(res);
	
											//Insert the order
											memset(queryStr,'\0',SIZE); 
											sprintf(queryStr,"INSERT INTO \"Order\" (order_id,payment_method,fk1_customer_id,fk2_product_id,comments,product_quantity_per_product) \
											VALUES(%d,'%s',%d,%d,'%s',%d)",order_id,payment_method,customer_id,product_id,comments,product_quantity);
											res=executeQuery(queryStr,connection,1);
											
											//Get the time
											time_t t = time(NULL);
											struct tm tm = *localtime(&t);
											
											//Clean memory
											PQclear(res);
	
											//Insert delivery										
											memset(queryStr,'\0',SIZE); 
											sprintf(queryStr,"INSERT INTO DELIVERY(delivery_id,departure_date,fk1_order_id,status) VALUES(%d,DATE '%d-%d-%d',%d,'Arrival within 2 days')" \
											,order_id,1900+tm.tm_year,tm.tm_mon+1,tm.tm_mday,order_id);
											
											res=executeQuery(queryStr,connection,1);
											
											printf("Order has been successfully added.\n");
											
											//Clear memory
											free(payment_method);
											free(comments);
											PQclear(res);
											break;
										}
									}
									
									break;
								}
							}
						}
					}
					
					// ----------------------------------------------- Show available products -----------------------------------------------------------------------------------------------------------
					else if(action==SHOW_PRODUCTS){
						showProducts(queryStr,connection);
					}
					
					// ----------------------------------------------- Show orders made ------------------------------------------------------------------------------------------------------------------
					else if(action==SHOW_ORDERS){
						showOrders(queryStr,connection,customer_id);
					}
					
					// ----------------------------------------------- Show total order cost --------------------------------------------------------------------------------------------------------------
					else if(action == TOTAL_COST){
						calculate_cost(queryStr,connection,customer_id);
					}
					// ----------------------------------------------- Cancel existing order -------------------------------------------------------------------------------------------------------------
					else if(action==CANCEL_ORDER){
						
						printf("Enter order_id: ");
						int order_id=checkInput();
						
						if(order_id<=0){
							printf("Invalid Input.\n");
						}
						else{
							//Search if the order exists
							memset(queryStr,'\0',SIZE); 
							sprintf(queryStr,"SELECT order_id FROM \"Order\" O where O.order_id = %d and fk1_customer_id= %d",order_id,customer_id);
							res=executeQuery(queryStr,connection,0);	
							
							if(PQntuples(res)==1){
								
								//Clean memory
								PQclear(res);
	
								memset(queryStr,'\0',SIZE); 
								sprintf(queryStr,"DELETE FROM \"Order\" O where O.order_id = %d",order_id);
								res=executeQuery(queryStr,connection,1);
								
								printf("The order was successfully deleted.\n");
								
								//Clean memory
								PQclear(res);
							}
							else{
								printf("You haven't made an order with that id.\n");
								//Clean memory
								PQclear(res);
							}
						}
					}
					// ----------------------------------------------- Logout -----------------------------------------------------------------------------------------------------------------------------
					else if(action==LOGOUT){
						break;
					}
					// ----------------------------------------------- Delete Customer Account -------------------------------------------------------------------------------------------------------------
					else if(action == DELETE_ACCOUNT){
						
						char choice='1';
						
						while(choice!='y' && choice!='Y' && choice!='n' && choice!='N'){
							printf("Are you sure you want to delete your account(y,n): ");
							scanf("%c",&choice);
						}
						
						if(choice=='y' || choice=='Y'){
							//Delete the user
							memset(queryStr,'\0',SIZE); 
							sprintf(queryStr,"DELETE FROM CUSTOMER C where C.customer_id=%d",customer_id);
							res=executeQuery(queryStr,connection,1);	
							
							//Auto logout
							printf("Account has been deleted successfully.");
							
							//Clean memory
							PQclear(res);
							break;		
						}
											
					}
					
				}
			}
		}
		// ----------------------------------------------- New Admin  -------------------------------------------------------------------------------------------------------------
		else if(opt==NEW_ADMIN){
			insertUsername(queryStr,username,connection,password,"ADMIN");
			
			// Count the admins
			memset(queryStr,'\0',SIZE); 
			sprintf(queryStr,"SELECT max(admin_id) FROM ADMIN");
			res=executeQuery(queryStr,connection,0);
			
			// New id of the new admin 
			int admin_id= atoi(PQgetvalue(res,0,0)) +1;
			
			//printf("After id : %d\n",admin_id);
			
			//Clean memory
			PQclear(res);

			// Format string for query 
			memset(queryStr,'\0',SIZE);
			sprintf(queryStr,"INSERT INTO ADMIN(password,username,admin_id) VALUES('%s','%s',%d)",password,username,admin_id);
			res=executeQuery(queryStr,connection,1);
			
			printf("Admin added successfully.");	
	
			// Clear the inputs 	
			memset(username,'\0',SIZE);
			memset(password,'\0',SIZE);
			
			//Clean memory
			PQclear(res);
		}
		// ----------------------------------------------- New Customer  ---------------------------------------------------------------------------------------------------------------------------
		else if(opt==NEW_CUSTOMER){
	
			// Customer variables 
			char *customer_email = (char *)malloc(sizeof(char)*SIZE);
			mallocCheck(customer_email);
			char *customer_lname = (char *)malloc(sizeof(char)*SIZE);
			mallocCheck(customer_lname);
			char *customer_fname = (char *)malloc(sizeof(char)*SIZE);
			mallocCheck(customer_fname);
			char *customer_phone = (char *)malloc(sizeof(char)*SIZE);
			mallocCheck(customer_phone);
		
			insertUsername(queryStr,username,connection,password,"CUSTOMER");
			
			printf("Firstname: ");
			readStr(customer_fname);
			printf("Lastname: ");
			readStr(customer_lname);
			printf("Email: ");
			readStr(customer_email);
			printf("Phone number: ");
			readStr(customer_phone);
				
			// Count the users
			memset(queryStr,'\0',SIZE);	
			sprintf(queryStr,"SELECT max(customer_id) FROM CUSTOMER");
			res=executeQuery(queryStr,connection,0);
			
			// New id of the new user
			int customer_id=atoi(PQgetvalue(res,0,0)) +1;
	
			//Clean memory
			PQclear(res);	
		
			// Format string for query
			memset(queryStr,'\0',SIZE);
			sprintf(queryStr,"INSERT INTO CUSTOMER(password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone) \
			VALUES('%s','%s','%s','%s','%s',%d,'%s')",password,username,customer_email,customer_lname,customer_fname,customer_id,customer_phone);
			res=executeQuery(queryStr,connection,1);
			
			printf("Customer added successfully.\n");	
			
			//Address Details
			
			char * address_name=(char *)malloc(sizeof(char)*SIZE);
			mallocCheck(address_name);
			char * address_city=(char *)malloc(sizeof(char)*SIZE);
			mallocCheck(address_city);
			char * address_country=(char *)malloc(sizeof(char)*SIZE);
			mallocCheck(address_country);
			
			memset(address_name,'\0',SIZE);
			memset(address_city,'\0',SIZE);
			memset(address_country,'\0',SIZE);
			
			int address_id;
			int address_number;
			int address_zipcode;
			
			printf("Enter address name: ");
			readStr(address_name);
			
			printf("Enter address number: ");
			address_number=checkInput();
			
			printf("Enter address city: ");
			readStr(address_city);
			
			printf("Enter address country: ");
			readStr(address_country);
		
			printf("Enter address zipcode: ");
			address_zipcode=checkInput();
			
			//Clean memory
			PQclear(res);

			// Check if the address exists
			memset(queryStr,'\0',SIZE);	
			sprintf(queryStr,"SELECT address_id FROM ADDRESS A where A.address_name ='%s' and A.address_number=%d \
			and A.address_zipcode =%d and A.address_city = '%s' and A.address_country='%s'",address_name,address_number,address_zipcode,address_city,address_country);
			res=executeQuery(queryStr,connection,0);
			
			if(PQntuples(res)==1){
				
				address_id=atoi(PQgetvalue(res,0,PQfnumber(res, "address_id")));
				
				//Clean	
				PQclear(res);
			
				//Update the customer
				memset(queryStr,'\0',SIZE);	
				sprintf(queryStr,"UPDATE CUSTOMER C SET fk1_address_id=%d where C.customer_id=%d",address_id,customer_id);
				res=executeQuery(queryStr,connection,1);
			}
			else{
				//Clean memory
				PQclear(res);

				//Count and get the next 
				memset(queryStr,'\0',SIZE);	
				sprintf(queryStr,"SELECT max(address_id) FROM ADDRESS");
				res=executeQuery(queryStr,connection,0);
				address_id=atoi(PQgetvalue(res,0,0))+1;
				
				//Clean memory
				PQclear(res);
			
				//Insert new address
				memset(queryStr,'\0',SIZE);	
				sprintf(queryStr,"INSERT INTO ADDRESS(address_country,address_city,address_zipcode,address_number,address_name,address_id) VALUES('%s','%s',%d,%d,'%s',%d)" \
				,address_country,address_city,address_zipcode,address_number,address_name,address_id);
				res=executeQuery(queryStr,connection,1);
				
				//Clean memory
				PQclear(res);
				
				//Update the customer
				memset(queryStr,'\0',SIZE);	
				sprintf(queryStr,"UPDATE CUSTOMER C SET fk1_address_id=%d where C.customer_id=%d",address_id,customer_id);
				res=executeQuery(queryStr,connection,1);
				
			}
			
			printf("Address was successfully added.\n");
			
			// Clear the inputs 	
			memset(username,'\0',SIZE);
			memset(password,'\0',SIZE);
			
			// Free memory
			free(customer_email);
			free(customer_lname);
			free(customer_fname);
			free(customer_phone);
			free(address_name);
			free(address_city);
			free(address_country);
			PQclear(res);
		}
		// ----------------------------------------------- Exit  -------------------------------------------------------------------------------------------------------------
		else if(opt==EXIT){
			break;
		}
	}
	
	//Free memory
	free(queryStr);
	free(username);
	free(password);

	PQfinish(connection);
	return 0;
}

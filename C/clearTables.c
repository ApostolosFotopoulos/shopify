#define SIZE 500
#include <stdio.h>
#include <stdlib.h>
#include <libpq-fe.h>
#include <string.h>


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

	//Delete all tables
	sprintf(queryStr,"DROP TABLE \"Order\" CASCADE");
	res=executeQuery(queryStr,connection,1);

	sprintf(queryStr,"DROP TABLE \"User\" CASCADE");
    res=executeQuery(queryStr,connection,1);	

	sprintf(queryStr,"DROP TABLE  ADDRESS CASCADE");
    res=executeQuery(queryStr,connection,1);

	sprintf(queryStr,"DROP TABLE  ADMIN CASCADE");
    res=executeQuery(queryStr,connection,1);

	sprintf(queryStr,"DROP TABLE  CUSTOMER CASCADE");
    res=executeQuery(queryStr,connection,1);
	
	sprintf(queryStr,"DROP TABLE  DELIVERY CASCADE");
    res=executeQuery(queryStr,connection,1);
	
	sprintf(queryStr,"DROP TABLE  PRODUCT CASCADE");
    res=executeQuery(queryStr,connection,1);

	sprintf(queryStr,"DROP TABLE  SUPPLIER CASCADE");
    res=executeQuery(queryStr,connection,1);
	return 0;

}

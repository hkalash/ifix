package com.smartsoftwaresolutions.ifix.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelper extends SQLiteOpenHelper {



public static SQLiteDatabase db = null;
    private static final String LOG = "DatabaseHelper";
    private static DataHelper mInstance = null;


    // Database Version
    private static final int DATABASE_VERSION =26;

    // Database Name in sqlite
    private static final String DATABASE_NAME = "ifixdb";

    private static final String CREATE_Service_Category_tbl="CREATE TABLE service_category_tbl (\n" +
            "    SC_id             INTEGER       PRIMARY KEY,\n" +
            "    SC_Descption    VARCHAR (500),\n" +
            "    SC_Description_ar VARCHAR (500), \n" +
            "    SC_Icon_path VARCHAR (500) ,\n" +
            "    SC_Icon_name VARCHAR (500) \n" +
            ");";
    // table name
    private static final String CREATE_Chiled_Service_Tbl="CREATE TABLE Chiled_Service_Table (\n" +
            "    Ch_ID          INT           PRIMARY KEY\n" +
            "                                 NOT NULL,\n" +
            "    MS_ID          INT           NOT NULL,\n" +
            "    Ch_Name        VARCHAR (250) NOT NULL,\n" +
            "    Ch_Description VARCHAR (500), \n" +
            "    Ch_Name_ar        VARCHAR (250) NOT NULL,\n" +
            "    Ch_Description_ar VARCHAR (500) \n" +
            ");\n";

    private static final String CREATE_country_Tbl="CREATE TABLE country (\n" +
            "    Country_ID          VARCHAR (11)  PRIMARY KEY ASC\n" +
            "                               NOT NULL,\n" +
            "    Country_name STRING (50) NOT NULL,\n" +
            "    Country_name_ar STRING (250) NOT NULL,\n" +
            "    Country_code STRING (5) NOT NULL\n" +
            ");\n";
    private static final String CREATE_sub_country_Tbl="CREATE TABLE country_sub_tbl (\n" +
            "    CS_ID          VARCHAR (5)  PRIMARY KEY ASC\n" +
            "                               NOT NULL,\n" +
            "    CS_name STRING (200) NOT NULL,\n" +
            "    CS_name_ar STRING (200) NOT NULL,\n" +
            "    Country_ID STRING (11) NOT NULL\n" +
            ");\n";

  private static final String CREATE_Master_Service_Tbl="CREATE TABLE Master_Service_tbl (\n" +
          "    MS_ID          INTEGER       PRIMARY KEY AUTOINCREMENT\n" +
          "                                 NOT NULL,\n" +
          "    C_ID           VARCHAR (2)   NOT NULL,\n" + //country_spinner
          "    SC_id             INTEGER      ,\n" +
          "    MS_Name        VARCHAR (250) NOT NULL,\n" +
          "    MS_Description VARCHAR (500), \n" +
          "    MS_Name_ar        VARCHAR (250) NOT NULL,\n" +
          "    MS_Description_ar VARCHAR (500), \n" +
          "    MS_Image_path VARCHAR (500), \n" +
          "    MS_Image_name VARCHAR (500) \n" +
          ");\n";
  private static final String CREATE_Order_A_Service_Reg_Tbl="CREATE TABLE Order_A_Service_Reg_Table (\n" +
          "    OASR_ID      INTEGER       PRIMARY KEY AUTOINCREMENT\n" +
          "                               NOT NULL,\n" +
          "    OASR_name    VARCHAR (150) NOT NULL,\n" +
          "    OASR_Mobile  VARCHAR (15)  NOT NULL,\n" +
          "    OASR_Phone   VARCHAR (15),\n" +
          "    OASR_Address VARCHAR (250) NOT NULL,\n" +
          "    OASR_Lat     FLOAT,\n" +
          "    OASR_Lon     FLOAT\n" +
          ");\n";
  private static final String CREATE_Order_Table_Tbl="CREATE TABLE Order_Table (\n" +
          "    O_ID          INTEGER       PRIMARY KEY AUTOINCREMENT\n" +
          "                                NOT NULL,\n" +
          "    OASR_ID       INTEGER       NOT NULL,\n" +
          "    Ch_ID         INTEGER       NOT NULL,\n" +
          "    SWM_ID        INTEGER       NOT NULL,\n" +
          "    O_Description VARCHAR (300) NOT NULL,\n" +
          "    O_Lat         FLOAT,\n" +
          "    O_Lon         FLOAT,\n" +
          "    O_condition   CHAR (1)      NOT NULL\n" + // if order is done or not
          ");\n";
  private static final String CREATE_Service_Worker_Child_Tbl="CREATE TABLE Service_Worker_Child_Table (\n" +
          "    SWC_ID    INTEGER PRIMARY KEY AUTOINCREMENT\n" +
          "                      NOT NULL,\n" +
          "    Ch_ID     INTEGER NOT NULL,\n" +
          "    SWM_ID    INTEGER NOT NULL,\n" +
          "    SWC_Price FLOAT\n" +
          ");\n";
  private static final String CREATE_Service_Worker_Master_Tbl="CREATE TABLE Service_Worker_Master_Table (\n" +
          "    SWM_ID      INTEGER       PRIMARY KEY AUTOINCREMENT\n" +
          "                              NOT NULL,\n" +
          "    SWM_Name    VARCHAR (100) NOT NULL,\n" +
          "    SWM_Phone   VARCHAR (15)  NOT NULL,\n" +
          "    SWM_Mobile  VARCHAR (15)  NOT NULL,\n" +
          "    SWM_Address VARCHAR (250) NOT NULL,\n" +
          "    SWM_Lat     FLOAT,\n" +
          "    SWM_Lon     FLOAT,\n" +
          "    SWM_Pic     VARCHAR ,\n" +
          "    SWM_Pic_name     VARCHAR\n" +
          ");\n";
    private static final String CREATE_User_Tbl="CREATE TABLE User_Tbl (\n" +
            "    User_Id       INTEGER       PRIMARY KEY AUTOINCREMENT\n" +
            "                                NOT NULL,\n" +
            "    User_Name     NVARCHAR (50) NOT NULL\n" +
            "                                COLLATE NOCASE,\n" +
            "    User_Password NVARCHAR (50) NOT NULL\n" +
            "                                COLLATE NOCASE,\n" +
            "    User_secret_ans NVARCHAR (250) NOT NULL\n" +
            "                                COLLATE NOCASE,\n" +
            "    User_Type_Id  INT           NOT NULL,\n" + // type cold premum and silver
            "    User_Server_ID      INTEGER(11),\n" +
            "    User_Phone   VARCHAR (15)  NOT NULL,\n" +
            "    User_Mobile  VARCHAR (15)  NOT NULL,\n" +
            "    User_Address VARCHAR (250) NOT NULL,\n" +
            "    User_company_name VARCHAR (250) ,\n" +
            "    User_web VARCHAR (250) ,\n" +
            "    User_email VARCHAR (250) ,\n" +
            "    User_Lat     FLOAT,\n" +
            "    User_Lon     FLOAT,\n" +
            "    User_Pic     VARCHAR ,\n" +
            "    User_Pic_name     VARCHAR\n" +
            ");";

    private static final String CREATE_Service_Selected_Tbl="CREATE TABLE Service_Selected_Tbl (\n" +
            "    SS_ID       INTEGER       PRIMARY KEY AUTOINCREMENT\n" +
            "                                NOT NULL,\n" +
            "    MS_ID      INTEGER  NOT NULL\n" +

            ");";

    private static final String CREATE_Service_Worker_Favorite_Tbl="CREATE TABLE Service_Worker_Favorite_Table (\n" +
            "    FSWM_ID      INTEGER       PRIMARY KEY AUTOINCREMENT\n" +
            "                              NOT NULL,\n" +
            "    FSWM_Name    VARCHAR (100) NOT NULL,\n" +
            "    FSWM_Phone   VARCHAR (15)  ,\n" +
            "    FSWM_Mobile  VARCHAR (15)  NOT NULL,\n" +
            "    FSWM_email  VARCHAR (100)  ,\n" +
            "    FSWM_Web  VARCHAR (100),  \n" +
            "    FSWM_Address VARCHAR (250) NOT NULL,\n" +
            "    FSWM_Lat     FLOAT,\n" +
            "    FSWM_Lon     FLOAT,\n" +
            "    FSWM_Pic     VARCHAR ,\n" +
            "    FSWM_company     VARCHAR(250) ,\n" +
            "    FSWM_Pic_name     VARCHAR\n" +
            ");\n";

    private static final String CREATE_IMAGES_TABLE ="CREATE TABLE Image_tbl (\n" +
            "    imgae_ID   INTEGER       PRIMARY KEY,\n" +
            "    Image      BLOB,\n" +
            "    Image_name VARCHAR (150),\n" +
            "    image_path VARCHAR (250), \n" +
            "    image_uri VARCHAR (250) \n" +
            ");";

    private static final String CREATE_LIKE_TABLE ="CREATE TABLE Like_tbl (\n" +
            "    Like_ID   INTEGER       PRIMARY KEY AUTOINCREMENT,\n" +
            "    SWM_ID      INTEGER,\n" +
            "    Like_   INTEGER default 0,\n" +
            "    Des_like INTEGER default 0 \n" +
                        ");";


    private static final String Drop_Chiled_Service_Tbl="DROP TABLE IF EXISTS Chiled_Service_Table";
    private static final String Drop_country_Tbl="DROP TABLE IF EXISTS country";
    private static final String Drop_sub_country_Tbl="DROP TABLE IF EXISTS country_sub_tbl";
    private static final String Drop_Master_Service_Tbl="DROP TABLE IF EXISTS Master_Service_tbl";
    private static final String Drop_Order_A_Service_Reg_Tbl="DROP TABLE IF EXISTS Order_A_Service_Reg_Table";
    private static final String Drop_Order_Table_Tbl="DROP TABLE IF EXISTS Order_Table";
    private static final String Drop_Service_Worker_Child_Tbl="DROP TABLE IF EXISTS Service_Worker_Child_Table";
    private static final String Drop_Service_Worker_Master_Tbl="DROP TABLE IF EXISTS Service_Worker_Master_Table";
    private static final String Drop_Service_Worker_Favorite_Tbl="DROP TABLE IF EXISTS Service_Worker_Favorite_Table";
    private static final String Drop_User_Tbl="DROP TABLE IF EXISTS User_Tbl";
    private static final String Drop_Service_Category_tbl="DROP TABLE IF EXISTS service_category_tbl";
    private static final String Drop_Service_Selected_Tbl="DROP TABLE IF EXISTS Service_Selected_Tbl";
    private static final String Drop_Image_Tbl="DROP TABLE IF EXISTS Image_tbl";
    private static final String Drop_Like_Tbl="DROP TABLE IF EXISTS Image_tbl";

    public DataHelper(Context context) {
        /**
         * context to use to open or create the database;
         name of the database file; commments.db is our case.
         factory to use for creating cursor objects, or null for the default; null in our case.
         version number of the database (starting at 1); if the database is older,
         onUpgrade(SQLiteDatabase, int, int) will be used to upgrade the database;
         if the database is newer,
         * */
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Chiled_Service_Tbl);
        db.execSQL(CREATE_country_Tbl);
        db.execSQL(CREATE_sub_country_Tbl);
        db.execSQL(CREATE_Master_Service_Tbl);
        db.execSQL(CREATE_Order_A_Service_Reg_Tbl);
        db.execSQL(CREATE_Order_Table_Tbl);
        db.execSQL(CREATE_Service_Worker_Child_Tbl);
        db.execSQL(CREATE_Service_Worker_Master_Tbl);
        db.execSQL(CREATE_Service_Worker_Favorite_Tbl);
        db.execSQL(CREATE_User_Tbl);
        db.execSQL(CREATE_Service_Category_tbl);
        db.execSQL(CREATE_Service_Selected_Tbl);
        db.execSQL(CREATE_IMAGES_TABLE);
        db.execSQL(CREATE_LIKE_TABLE);

    }
    // on update of sql version
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(Drop_country_Tbl);
        db.execSQL(Drop_sub_country_Tbl);
        db.execSQL(Drop_Chiled_Service_Tbl);
        db.execSQL(Drop_Master_Service_Tbl);
        db.execSQL(Drop_Order_A_Service_Reg_Tbl);
        db.execSQL(Drop_Order_Table_Tbl);
        db.execSQL(Drop_Service_Worker_Child_Tbl);
        db.execSQL(Drop_Service_Worker_Master_Tbl);
        db.execSQL(Drop_Service_Worker_Favorite_Tbl);
        db.execSQL(Drop_User_Tbl);
        db.execSQL(Drop_Service_Category_tbl);
        db.execSQL(Drop_Service_Selected_Tbl);
        db.execSQL(Drop_Image_Tbl);
        db.execSQL(Drop_Like_Tbl);


        // create new tables
        onCreate(db);
    }


}

/**
 * Database class
 */
package com.example.demo;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

// SQLite References
// The SQLite Database code was learned and implemented from multiple sources:
// 1) ProgrammingKnowledge Tutorial (6 parts)
// https://www.youtube.com/watch?v=cp2rL3sAFmI&list=PLS1QulWo1RIaRdy16cOzBO5Jr6kEagA07
// 2) Android Official Documentation
// https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase

public class Database extends SQLiteOpenHelper {

    // DATABASE NAME
    private static final String DATABASE_NAME = "hummus-v14.db";

    // LOGIN TABLE
    private static final String LOGIN_TABLE_NAME = "login_table";
    private static final String LOGIN_ID = "ID";
    private static final String LOGIN_STATUS = "status";
    private static final String LOGIN_USER = "user";

    // USERS TABLE COLUMNS
    private static final String USER_TABLE_NAME = "user_table";
    private static final String USER_ID = "ID";
    private static final String USER_USERNAME = "username";
    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "password";
    private static final String USER_SCORE = "score";
    private static final String USER_HEX = "hex";
    private static final String USER_LOCATION = "location";
    private static final String USER_IMAGE_PATH = "image_path";

    // CHALLENGE TABLE COLUMNS
    private static final String CHALLENGE_TABLE_NAME = "challenge_table";
    private static final String CHALLENGE_ID = "ID";
    private static final String CHALLENGE_TITLE = "title";
    private static final String CHALLENGE_TAG = "tag";
    private static final String CHALLENGE_DESCRIPTION = "description";
    private static final String CHALLENGE_COORDINATES = "coordinates";
    private static final String CHALLENGE_LEVEL = "level";
    private static final String CHALLENGE_SCORE = "score";
    private static final String CHALLENGE_CREATOR_ID = "creator_id";
    private static final String CHALLENGE_DEADLINE = "deadline";
    private static final String CHALLENGE_HEX = "hex";

    // GATEWAY TABLE COLUMNS
    private static final String GATEWAY_TABLE_NAME = "gateway_table";
    private static final String GATEWAY_ID = "id";
    private static final String GATEWAY_USER_ID = "user_id";
    private static final String GATEWAY_CHALLENGE_ID = "challenge_id";

    // FOLLOWING TABLE COLUMNS
    private static final String FOLLOWING_TABLE_NAME = "following_table";
    private static final String FOLLOWING_ID = "id";
    private static final String FOLLOWING_USER1_ID = "user1_id";
    private static final String FOLLOWING_USER2_ID = "user2_id";

    // SQL STATEMENT - CREATE USERS TABLE
    private static final String CREATE_LOGIN_TABLE = "CREATE TABLE " + LOGIN_TABLE_NAME +
            " (ID INTEGER PRIMARY KEY AUTOINCREMENT, STATUS INTEGER, USER INTEGER)";

    // SQL STATEMENT - CREATE USERS TABLE
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + USER_TABLE_NAME +
            " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, EMAIL TEXT, PASSWORD TEXT, SCORE INTEGER, HEX TEXT, LOCATION TEXT, IMAGE_PATH TEXT)";

    // SQL STATEMENT - CREATE CHALLENGES TABLE
    private static final String CREATE_CHALLENGES_TABLE = "CREATE TABLE " + CHALLENGE_TABLE_NAME +
            " (ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT, TAG TEXT, DESCRIPTION TEXT, COORDINATES TEXT, LEVEL TEXT, SCORE INTEGER, CREATOR_ID INTEGER, DEADLINE TEXT, HEX TEXT)";

    // SQL STATEMENT - CREATE GATEWAY TABLE
    private static final String CREATE_GATEWAY_TABLE = "CREATE TABLE " + GATEWAY_TABLE_NAME +
            " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_ID INTEGER, CHALLENGE_ID INTEGER)";

    // SQL STATEMENT - CREATE FOLLOWING TABLE
    private static final String CREATE_FOLLOWING_TABLE = "CREATE TABLE " + FOLLOWING_TABLE_NAME +
            " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USER1_ID INTEGER, USER2_ID INTEGER)";

    // DATABASE CONTEXT
    public Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // EXECUTES UPON DATABASE CREATION
    public void onCreate(SQLiteDatabase db){

        // EXECUTES SQL STATEMENTS TO CREATE ALL TABLES
        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_CHALLENGES_TABLE);
        db.execSQL(CREATE_GATEWAY_TABLE);
        db.execSQL(CREATE_FOLLOWING_TABLE);

        db.execSQL("INSERT INTO " + LOGIN_TABLE_NAME + "(STATUS, USER ) VALUES (0, 0)");

        // INSERT PRE-BUILT USERS
        db.execSQL("INSERT INTO " + USER_TABLE_NAME + "(USERNAME, EMAIL, PASSWORD, SCORE, HEX, LOCATION, IMAGE_PATH ) VALUES ('seant', 'sean.thomas1@ucdconnect.ie', 'st84403', 200, '#228B22', 'Dublin', 'noimage')");
        db.execSQL("INSERT INTO " + USER_TABLE_NAME + "(USERNAME, EMAIL, PASSWORD, SCORE, HEX, LOCATION, IMAGE_PATH ) VALUES ('ivans', 'ivan.sladkov@ucdconnect.ie', 'is45655', 550, '#228B22', 'Dublin', 'noimage')");
        db.execSQL("INSERT INTO " + USER_TABLE_NAME + "(USERNAME, EMAIL, PASSWORD, SCORE, HEX, LOCATION, IMAGE_PATH ) VALUES ('hanyuanq', 'hanyuan.qu@ucdconnect.ie', 'hq22610', 1050, '#228B22', 'Dublin', 'noimage')");
        db.execSQL("INSERT INTO " + USER_TABLE_NAME + "(USERNAME, EMAIL, PASSWORD, SCORE, HEX, LOCATION, IMAGE_PATH ) VALUES ('jashjv', 'jashwanth.jegadeesanvijaya@ucdconnect.ie', '#jj61173', 500, '#228B22', 'Dublin', 'noimage')");
        db.execSQL("INSERT INTO " + USER_TABLE_NAME + "(USERNAME, EMAIL, PASSWORD, SCORE, HEX, LOCATION, IMAGE_PATH ) VALUES ('shauryag', 'shaurya.gogia@ucdconnect.ie', 'sg69817', 425, '#228B22', 'Dublin', 'noimage')");
        db.execSQL("INSERT INTO " + USER_TABLE_NAME + "(USERNAME, EMAIL, PASSWORD, SCORE, HEX, LOCATION, IMAGE_PATH ) VALUES ('cathalp', 'cathal.peelo@ucdconnect.ie', 'cp96033', 425, '#228B22', 'Dublin', 'noimage')");
        db.execSQL("INSERT INTO " + USER_TABLE_NAME + "(USERNAME, EMAIL, PASSWORD, SCORE, HEX, LOCATION, IMAGE_PATH ) VALUES ('davidc', 'd.coyle@ucd.ie', 'dc13241', 305, '#228B22', 'Dublin', 'noimage')");
        db.execSQL("INSERT INTO " + USER_TABLE_NAME + "(USERNAME, EMAIL, PASSWORD, SCORE, HEX, LOCATION, IMAGE_PATH ) VALUES ('eoinon', 'eoin.o-neill.3@ucdconnect.ie', 'en46504', 395, '#228B22', 'Dublin', 'noimage')");
        db.execSQL("INSERT INTO " + USER_TABLE_NAME + "(USERNAME, EMAIL, PASSWORD, SCORE, HEX, LOCATION, IMAGE_PATH ) VALUES ('nicolasc', 'nicolas.cage@netflix.com', 'nc66666', 195, '#228B22', 'London', 'noimage')");
        db.execSQL("INSERT INTO " + USER_TABLE_NAME + "(USERNAME, EMAIL, PASSWORD, SCORE, HEX, LOCATION, IMAGE_PATH ) VALUES ('keanur', 'keanu.reevese@netflix.com', 'kr12345', 675, '#228B22', 'Toronto', 'noimage')");
        db.execSQL("INSERT INTO " + USER_TABLE_NAME + "(USERNAME, EMAIL, PASSWORD, SCORE, HEX, LOCATION, IMAGE_PATH ) VALUES ('matressmick', 'mick@matressmick.ie', 'magicalmick', 205, '#228B22', 'Dublin', 'noimage')");

        db.execSQL("INSERT INTO " + CHALLENGE_TABLE_NAME + "(TITLE, TAG, DESCRIPTION, COORDINATES, LEVEL, SCORE, CREATOR_ID, DEADLINE, HEX) " +
                "VALUES ('Marlay Walk', 'walk', 'Go for a 20 minute walk in Marlay Park', '53.309063,-6.218026', 'LOCAL', 15, 6, '2020-01-01', '#FFC312')");
        db.execSQL("INSERT INTO " + CHALLENGE_TABLE_NAME + "(TITLE, TAG, DESCRIPTION, COORDINATES, LEVEL, SCORE, CREATOR_ID, DEADLINE, HEX) " +
                "VALUES ('Lights Out', 'electricity', 'Turn off all the lights for one hour', 'NA', 'GLOBAL', 20, 1, '2020-01-01', '#9980FA')");
        db.execSQL("INSERT INTO " + CHALLENGE_TABLE_NAME + "(TITLE, TAG, DESCRIPTION, COORDINATES, LEVEL, SCORE, CREATOR_ID, DEADLINE, HEX) " +
                "VALUES ('Shorter Shower', 'water', 'Take a shower using 50% less water', 'NA', 'GLOBAL', 10, 4, '2020-01-01', '#FFC312')");
        db.execSQL("INSERT INTO " + CHALLENGE_TABLE_NAME + "(TITLE, TAG, DESCRIPTION, COORDINATES, LEVEL, SCORE, CREATOR_ID, DEADLINE, HEX) " +
                "VALUES ('Clean UCD', 'litter', 'Pick up some litter in UCD and discard of it appropriately', '53.309063,-6.218026', 'LOCAL', 15, 7, '2020-01-01', '#9980FA')");
        db.execSQL("INSERT INTO " + CHALLENGE_TABLE_NAME + "(TITLE, TAG, DESCRIPTION, COORDINATES, LEVEL, SCORE, CREATOR_ID, DEADLINE, HEX) " +
                "VALUES ('Compost Champion', 'water', 'Compost your food waste', '53.309063,-6.218026', 'COUNTRY', 5, 5, '2020-01-01', '#FFC312')");
        db.execSQL("INSERT INTO " + CHALLENGE_TABLE_NAME + "(TITLE, TAG, DESCRIPTION, COORDINATES, LEVEL, SCORE, CREATOR_ID, DEADLINE, HEX) " +
                "VALUES ('Dun Laoghaire Pier', 'walk', 'Walk Dun Laoghaire Pier', '53.309063,-6.218026', 'LOCAL', 20, 2, '2020-01-01', '#9980FA')");
        db.execSQL("INSERT INTO " + CHALLENGE_TABLE_NAME + "(TITLE, TAG, DESCRIPTION, COORDINATES, LEVEL, SCORE, CREATOR_ID, DEADLINE, HEX) " +
                "VALUES ('Reusable Cup', 'food', 'Use a reusable cup in Dundrum', '53.309063,-6.218026', 'LOCAL', 10, 1, '2020-01-01', '#FFC312')");


        // INSERT PRE-BUILT GATEWAY RELATIONSHIPS
        db.execSQL("INSERT INTO " + GATEWAY_TABLE_NAME + "(USER_ID, CHALLENGE_ID ) VALUES (1, 2)");
        db.execSQL("INSERT INTO " + GATEWAY_TABLE_NAME + "(USER_ID, CHALLENGE_ID ) VALUES (1, 5)");
        db.execSQL("INSERT INTO " + GATEWAY_TABLE_NAME + "(USER_ID, CHALLENGE_ID ) VALUES (1, 3)");
        db.execSQL("INSERT INTO " + GATEWAY_TABLE_NAME + "(USER_ID, CHALLENGE_ID ) VALUES (1, 4)");
        db.execSQL("INSERT INTO " + GATEWAY_TABLE_NAME + "(USER_ID, CHALLENGE_ID ) VALUES (2, 1)");
        db.execSQL("INSERT INTO " + GATEWAY_TABLE_NAME + "(USER_ID, CHALLENGE_ID ) VALUES (3, 4)");
        db.execSQL("INSERT INTO " + GATEWAY_TABLE_NAME + "(USER_ID, CHALLENGE_ID ) VALUES (3, 5)");
        db.execSQL("INSERT INTO " + GATEWAY_TABLE_NAME + "(USER_ID, CHALLENGE_ID ) VALUES (3, 7)");
        db.execSQL("INSERT INTO " + GATEWAY_TABLE_NAME + "(USER_ID, CHALLENGE_ID ) VALUES (4, 3)");
        db.execSQL("INSERT INTO " + GATEWAY_TABLE_NAME + "(USER_ID, CHALLENGE_ID ) VALUES (4, 6)");
        db.execSQL("INSERT INTO " + GATEWAY_TABLE_NAME + "(USER_ID, CHALLENGE_ID ) VALUES (5, 2)");
        db.execSQL("INSERT INTO " + GATEWAY_TABLE_NAME + "(USER_ID, CHALLENGE_ID ) VALUES (5, 1)");
        db.execSQL("INSERT INTO " + GATEWAY_TABLE_NAME + "(USER_ID, CHALLENGE_ID ) VALUES (6, 3)");
        db.execSQL("INSERT INTO " + GATEWAY_TABLE_NAME + "(USER_ID, CHALLENGE_ID ) VALUES (8, 1)");

        // INSERT PRE-BUILT FOLLOWER/FOLLOWING RELATIONSHIPS
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (1, 2)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (1, 3)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (1, 7)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (1, 8)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (6, 7)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (2, 7)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (2, 3)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (3, 4)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (3, 5)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (4, 1)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (4, 5)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (5, 7)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (7, 1)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (7, 2)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (7, 3)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (7, 4)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (8, 3)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (8, 4)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (8, 5)");
        db.execSQL("INSERT INTO " + FOLLOWING_TABLE_NAME + "(USER1_ID, USER2_ID ) VALUES (8, 6)");

    }

    // UPON POTENTIAL UPGRADE, DROP EACH TABLE AND REBUILD
    public void onUpgrade(SQLiteDatabase db, int previousVersion, int nextVersion){
        db.execSQL("DROP TABLE IF EXISTS " + LOGIN_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CHALLENGE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GATEWAY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FOLLOWING_TABLE_NAME);
        onCreate(db);
    }

    // INSERT A NEW USER
    public boolean insertUser(String USERNAME, String EMAIL, String PASSWORD, int SCORE, String HEX, String LOCATION, String IMAGE_PATH){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues userValues = new ContentValues();
        userValues.put(USER_USERNAME, USERNAME);
        userValues.put(USER_EMAIL, EMAIL);
        userValues.put(USER_PASSWORD, PASSWORD);
        userValues.put(USER_SCORE, SCORE);
        userValues.put(USER_HEX, HEX);
        userValues.put(USER_LOCATION, SCORE);
        userValues.put(USER_IMAGE_PATH, IMAGE_PATH);
        Log.d("tag", userValues.toString());
        long result = db.insert(USER_TABLE_NAME, null, userValues);

        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    // IS USER LOGGED IN?  (0 = FALSE, 1 = TRUE)
    public int getStatus(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + LOGIN_TABLE_NAME,null);
        StringBuffer buffer = new StringBuffer();
        int status = 0;

        while(result.moveToNext()) {
            status = result.getInt(1);
        }

        return status;
    }

    // SET WHETHER A USER HAS LOGGED IN OR LOGGED OUT
    public boolean setStatus(boolean STATUS){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        if (STATUS == true) {
            contentValues.put(LOGIN_STATUS, 1);
        } else{
            contentValues.put(LOGIN_STATUS, 0);
        }
        db.update(LOGIN_TABLE_NAME, contentValues, "ID = ?", new String[] {"1"} );

        return true;
    }

    // WHICH USER IS LOGGED IN? (RETURN ID)
    public int getUser(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + LOGIN_TABLE_NAME,null);
        //StringBuffer buffer = new StringBuffer();
        int user = 0;

        while(result.moveToNext()) {
            user = result.getInt(2);
        }

        return user;
    }

    public int getUserIDByUserName(String USERNAME){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE USERNAME = '" + USERNAME + "'", null);
        int id = -1;
        while (result.moveToNext()){
            id = result.getInt(0);
        }
        return id;
    }

    // EDIT WHICH USER IS LOGGED IN
    public boolean setUser(int USER_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOGIN_USER, USER_ID);
        db.update(LOGIN_TABLE_NAME, contentValues, "ID = ?", new String[] {"1"} );

        return true;
    }

    // OBTAIN DATA TO VERIFY THAT THE INPUTTER USERNAME & PASSWORD MATCH
    public String loginConfirm(String USERNAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        String key = "";
        Cursor result = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE USERNAME = '" + USERNAME + "'", null);
        StringBuffer buffer = new StringBuffer();

        String NAME = USERNAME;

        while (result.moveToNext()) {
            buffer.append(result.getString(3));
            key = result.getString(3);
        }
        return key;
    }

    // GET A CURSOR OF ALL USERS
    public Cursor getAllUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME,null);

        return result;
    }

    // GET THE TOP (INPUT) USERS
    public Cursor getTop(int INPUT){
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor result = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " ORDER BY SCORE DESC",null);
        Cursor result = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " ORDER BY SCORE DESC LIMIT " + INPUT,null);

        return result;
    }

    // GET ALL USERS RANKED BY SCORE (DESCENDING)
    public Cursor getTopAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor result = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " ORDER BY SCORE DESC",null);
        Cursor result = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " ORDER BY SCORE DESC",null);

        return result;
    }

    // GET A CURSOR OF ALL CHALLENGES
    public Cursor getChallengeObjects(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + CHALLENGE_TABLE_NAME,null);

        return result;
    }

    // GET A USER BY ID
    public String getUser(int USER_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE ID = " + USER_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()){
            buffer.append("ID: " + result.getString(0) + "\n");
            buffer.append("USERNAME: " + result.getString(1) + "\n");
            buffer.append("EMAIL: " + result.getString(2) + "\n");
            buffer.append("PASSWORD: " + result.getString(3) + "\n");
            buffer.append("SCORE: " + result.getInt(4) + "\n");
            buffer.append("HEX: " + result.getString(5) + "\n");
            buffer.append("LOCATION: " + result.getString(6) + "\n");
            buffer.append("IMAGE_PATH: " + result.getString(7) + "\n");
        }

        return buffer.toString();
    }

    // GET A USER'S USERNAME BY ID
    public String getUserUsername(int USER_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE ID = " + USER_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()) {
            buffer.append(result.getString(1));
        }

        return buffer.toString();
    }

    // GET A USER'S EMAIL BY ID
    public String getUserEmail(int USER_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE ID = " + USER_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()) {
            buffer.append(result.getString(2));
        }

        return buffer.toString();
    }

    // GET A USER'S PASSWORD BY ID
    public String getUserPassword(int USER_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE ID = " + USER_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()) {
            buffer.append(result.getString(3));
        }

        return buffer.toString();
    }

    // GET A USER'S SCORE BY ID
    public String getUserScore(int USER_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE ID = " + USER_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()) {
            buffer.append(result.getString(4));
        }

        return buffer.toString();
    }

    // GET A USER'S ACTIVE HEXADECIMAL COLOR BY ID
    public String getUserHex(int USER_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE ID = " + USER_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()) {
            buffer.append(result.getString(5));
        }

        return buffer.toString();
    }

    // GET A USER'S LOCATION BY ID
    public String getUserLocation(int USER_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE ID = " + USER_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()) {
            buffer.append(result.getString(6));
        }

        return buffer.toString();
    }

    // GET A USER'S PROFILE IMAGE PATH BY ID
    public String getUserImagePath(int USER_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE ID = " + USER_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()) {
            buffer.append(result.getString(7));
        }

        return buffer.toString();
    }

    // UPDATE A USER'S PROFILE IMAGE PATH BY ID
    public boolean updateUserImagePath(int ID, String IMAGE_PATH){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_IMAGE_PATH, IMAGE_PATH);
        db.update(USER_TABLE_NAME, contentValues, "ID = ?", new String[] {ID + ""} );

        return true;
    }

    // SELECT A USER BY THEIR ID AND UPDATE THEIR SCORE
    public boolean updateUserScore(int ID, int SCORE){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_SCORE, SCORE);

        db.update(USER_TABLE_NAME, contentValues, "ID = ?", new String[] {ID + ""} );

        return true;
    }

    // UPDATE A USER'S PROFILE IMAGE PATH BY ID
    public boolean updateUserHex(int ID, String HEX){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_HEX, HEX);
        db.update(USER_TABLE_NAME, contentValues, "ID = ?", new String[] {ID + ""} );

        return true;
    }

    // UPDATE A USER'S PROFILE IMAGE PATH BY ID
    public boolean updateUserName(int ID, String USERNAME){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_USERNAME, USERNAME);
        db.update(USER_TABLE_NAME, contentValues, "ID = ?", new String[] {ID + ""} );

        return true;
    }

    // SELECT A USER BY THEIR ID AND UPDATE ALL THEIR ATTRIBUTES
    public boolean updateUser(int ID, String NAME, String EMAIL, String PASSWORD, int SCORE, String HEX, String LOCATION){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_USERNAME, NAME);
        contentValues.put(USER_EMAIL, EMAIL);
        contentValues.put(USER_PASSWORD, PASSWORD);
        contentValues.put(USER_SCORE, SCORE);
        contentValues.put(USER_HEX, HEX);
        contentValues.put(USER_LOCATION, LOCATION);
        db.update(USER_TABLE_NAME, contentValues, "ID = ?", new String[] {ID + ""} );

        return true;
    }

    // DELETE A USER BY ID
    public Integer deleteUser(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(USER_TABLE_NAME, "ID = ?", new String[] {ID + ""});
    }

    // INSERT A NEW CHALLENGE
    public boolean insertChallenge(String TITLE, String TAG, String DESCRIPTION, String COORDINATES, String LEVEL, int SCORE, int CREATOR_ID, String DEADLINE, String HEX){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues challengeValues = new ContentValues();

        challengeValues.put(CHALLENGE_TITLE, TITLE);
        challengeValues.put(CHALLENGE_TAG, TAG);
        challengeValues.put(CHALLENGE_DESCRIPTION, DESCRIPTION);
        challengeValues.put(CHALLENGE_COORDINATES, COORDINATES);
        challengeValues.put(CHALLENGE_LEVEL, LEVEL);
        challengeValues.put(CHALLENGE_SCORE, SCORE);
        challengeValues.put(CHALLENGE_CREATOR_ID, CREATOR_ID);
        challengeValues.put(CHALLENGE_DEADLINE, DEADLINE);
        challengeValues.put(CHALLENGE_HEX, HEX);

        long result = db.insert(CHALLENGE_TABLE_NAME, null, challengeValues);

        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    // GET A CURSOR OF ALL CHALLENGES
    public Cursor getAllChallenges(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + CHALLENGE_TABLE_NAME,null);

        return res;
    }

    // GET A CHALLENGE BY ID
    public String getChallenge(int CHALLENGE_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + CHALLENGE_TABLE_NAME + " WHERE ID = " + CHALLENGE_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()){
            buffer.append("ID: " + result.getString(0) + "\n");
            buffer.append("TITLE: " + result.getString(1) + "\n");
            buffer.append("TAG: " + result.getString(2) + "\n");
            buffer.append("DESCRIPTION: " + result.getString(3) + "\n");
            buffer.append("COORDINATES: " + result.getString(4) + "\n");
            buffer.append("LEVEL: " + result.getString(5) + "\n");
            buffer.append("SCORE: " + result.getInt(6) + "\n");
            buffer.append("DURATION: " + result.getInt(7) + "\n");
            buffer.append("CREATOR_ID: " + result.getInt(8) + "\n \n");
            buffer.append("DEADLINE: " + result.getInt(9) + "\n \n");
        }

        return buffer.toString();
    }

    // GET THE TOTAL NUMBER OF CHALLENGES
    public int getChallengeCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + CHALLENGE_TABLE_NAME,null);

        int count = 0;
        while(result.moveToNext()){
            count++;
        }

        return count;
    }

    // GET A CHALLENGE'S TITLE BY ID
    public String getChallengeTitle(int CHALLENGE_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + CHALLENGE_TABLE_NAME + " WHERE ID = " + CHALLENGE_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()){
            buffer.append(result.getString(1));
        }

        return buffer.toString();
    }

    // GET A CHALLENGE'S TAG BY ID
    public String getChallengeTag(int CHALLENGE_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + CHALLENGE_TABLE_NAME + " WHERE ID = " + CHALLENGE_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()){
            buffer.append(result.getString(2));
        }

        return buffer.toString();
    }

    // GET A CHALLENGE'S DESCRIPTION BY ID
    public String getChallengeDescription(int CHALLENGE_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + CHALLENGE_TABLE_NAME + " WHERE ID = " + CHALLENGE_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()){
            buffer.append(result.getString(3));
        }

        return buffer.toString();
    }

    // GET A CHALLENGE'S COORDINATES BY ID
    public String getChallengeCoordinates(int CHALLENGE_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + CHALLENGE_TABLE_NAME + " WHERE ID = " + CHALLENGE_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()){
            buffer.append(result.getString(4));
        }

        return buffer.toString();
    }

    // GET A CHALLENGE'S LEVEL BY ID
    public String getChallengeLevel(int CHALLENGE_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + CHALLENGE_TABLE_NAME + " WHERE ID = " + CHALLENGE_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()){
            buffer.append(result.getString(5));
        }

        return buffer.toString();
    }

    // GET A CHALLENGE'S SCORE BY ID
    public String getChallengeScore(int CHALLENGE_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + CHALLENGE_TABLE_NAME + " WHERE ID = " + CHALLENGE_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()){
            buffer.append(result.getString(6));
        }

        return buffer.toString();
    }

    // INPUT A CHALLENGE ID AND OUTPUT THE CREATOR ID
    public String getChallengeCreatorID(int CHALLENGE_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + CHALLENGE_TABLE_NAME + " WHERE ID = " + CHALLENGE_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()){
            buffer.append(result.getString(7));
        }

        return buffer.toString();
    }

    // INPUT A CHALLENGE ID AND OUTPUT THE DEADLINE
    public String getChallengeDeadline(int CHALLENGE_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + CHALLENGE_TABLE_NAME + " WHERE ID = " + CHALLENGE_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()){
            buffer.append(result.getString(8));
        }

        return buffer.toString();
    }

    // INPUT A CHALLENGE ID AND OUTPUT THE HEX COLOUR
    public String getChallengeHex(int CHALLENGE_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + CHALLENGE_TABLE_NAME + " WHERE ID = " + CHALLENGE_ID,null);
        StringBuffer buffer = new StringBuffer();

        while(result.moveToNext()){
            buffer.append(result.getString(9));
        }

        return buffer.toString();
    }

    // SELECT A CHALLENGE BY ID AND UPDATE ALL THE ATTRIBUTES
    public boolean updateChallenge(int ID, String TITLE, String TAG, String DESCRIPTION, String COORDINATES, String LEVEL, int SCORE, int CREATOR_ID, String DEADLINE, String HEX){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues challengeValues = new ContentValues();

        challengeValues.put(CHALLENGE_TITLE, TITLE);
        challengeValues.put(CHALLENGE_TAG, TAG);
        challengeValues.put(CHALLENGE_DESCRIPTION, DESCRIPTION);
        challengeValues.put(CHALLENGE_COORDINATES, COORDINATES);
        challengeValues.put(CHALLENGE_LEVEL, LEVEL);
        challengeValues.put(CHALLENGE_SCORE, SCORE);
        challengeValues.put(CHALLENGE_CREATOR_ID, CREATOR_ID);
        challengeValues.put(CHALLENGE_DEADLINE, DEADLINE);
        challengeValues.put(CHALLENGE_HEX, HEX);

        db.update(CHALLENGE_TABLE_NAME, challengeValues, "ID = ?", new String[] {ID + ""} );
        return true;
    }

    // DELETE A CHALLENGE BY ID
    public Integer deleteChallenge(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(CHALLENGE_TABLE_NAME, "ID = ?", new String[] {ID + ""});
    }

    // GET A CURSOR OF ALL GATEWAY INFORMATION
    public Cursor getAllCompleted(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + GATEWAY_TABLE_NAME,null);

        return res;
    }

    // INSERT A CHALLENGE COMPLETION INTO THE GATEWAY TABLE
    public boolean completedChallenge(int USER_ID, int CHALLENGE_ID){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues completedChallenge = new ContentValues();
        completedChallenge.put(GATEWAY_USER_ID, USER_ID + "");
        completedChallenge.put(GATEWAY_CHALLENGE_ID, CHALLENGE_ID + "");
        Log.d("tag", completedChallenge.toString());
        long result = db.insert(GATEWAY_TABLE_NAME, null, completedChallenge);

        if (result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    // GET A CURSOR OF COMPLETED CHALLENGES FROM THE GATEWAY BY USER ID
    public Cursor getCompletedChallenges(int INPUT){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + GATEWAY_TABLE_NAME + " WHERE USER_ID = " + INPUT,null);

        return result;
    }

    // RETRIEVE IDs OF ALL USERS WHICH THE INPUTTED USER IS FOLLOWING
    public Cursor getFollowing(int USER_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT USER2_ID FROM " + FOLLOWING_TABLE_NAME + " WHERE USER1_ID = " + USER_ID,null);

        return result;
    }

    // RETRIEVE IDs OF ALL USERS WHICH THE INPUTTED USER IS FOLLOWED BY
    public Cursor getFollowers(int USER_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT USER1_ID FROM " + FOLLOWING_TABLE_NAME + " WHERE USER2_ID = " + USER_ID,null);

        return result;
    }

}
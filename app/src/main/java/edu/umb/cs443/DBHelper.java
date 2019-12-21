package edu.umb.cs443;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import java.util.LinkedList;


public class DBHelper extends SQLiteOpenHelper {


    private static Integer Version = 1;
    private static final String DATABASE_NAME = "Parking";
    private static final String DATABASE_TABLE = "Parking_lot";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, Version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Parking_lot(id INTEGER primary key,name TEXT, " +
                "latitude float,longitude float, address TEXT, charges INTEGER)";

        db.execSQL(sql);
    }

    public  void insertData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
       String sql = "insert into Parking_lot(id, name, latitude, longitude,address,charges)" +
                "values " +
                "(1,'JKF Library Parking', 42.3151073, -71.0343146,'Columbia Point, Boston, MA 02125',5)," +
                "(2,'University Lot D', 42.3170906, -71.0378873,'University Dr N, Dorchester, MA 02125',6)," +
                "(3,'West Garage, UMass Boston', 42.3152501, -71.0412884,'University Dr W, Boston, MA 02125',4)," +
                "(4,'Municipal Lot #019', 42.3181774, -71.0653532,'19 Hamlet St, Boston, MA 02125',6)," +
                "(5,'Municipal Lot #20',42.3023966, -71.0606217,'191 Adams St, Boston, MA 02122',7)," +
                "(6,'South Bay Parking Garage', 42.3252137, -71.0627782,'95 Allstate Rd, Boston, MA 02125',10)," +
                "(7,'63 R Boston Street Parking', 42.3282675, -71.0596561,'63R Boston St, Boston, MA 02125',8)," +
                "(8,'6 Douglas St Parking', 42.3310753, -71.0425973,'6 Douglas St, Boston, MA 02127',6)," +
                "(9,'589 E 7th St Parking', 42.3320112,-71.0368145,'589E E 7th St, Boston, MA 02127',5)," +
                "(10,'205-229 W 7th St Parking',42.3336927,-71.0505259,'205-229 W 7th St, Boston, MA 02127',7)," +
                "(11,'451-455A E Broadway Parking',42.3357389, -71.0467494,'451-455A E Broadway, Boston, MA 02127',5)," +
                "(12,'Harding Ct Parking',42.3374757,-71.0519958,'Harding Ct, Boston, MA 02127',6)," +
                "(13,'2-90 E 1st St Parking',42.3382529,-71.0442173,'2-90 E 1st St, Boston, MA 02127',7)," +
                "(14,'6 Elkins St Parking',42.3386732,-71.036278,'6 Elkins St, Boston, MA 02127',8)," +
                "(15,'The Lot on D Parking',42.342559,-71.0457194,'371-377 D St, Boston, MA 02210',10)," +
                "(16,'Channel Center Garage',42.3434789,-71.052146,'116 W First St, Boston, MA 02127',9)," +
                "(17,'Channelside Lot',42.3480622,-71.0517704,'45 Binford St, Boston, MA 02210',5)," +
                "(18,'Park Lane Garage',42.3484032,-71.0385525,'225 Northern Ave, Boston, MA 02210',5)," +
                "(19,'660 Washington Street Garage',42.3518206,-71.0623598,'660 Washington St, Boston, MA 02110',6)," +
                "(20,'Boston Common Garage',42.3540089,-71.0680676,'0 Charles St, Boston, MA 02116',4)";

               db.execSQL(sql);
                db.close();
    }






    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

        // CREATE TABLE AGAIN
        onCreate(db);
    }



    public parkingLot getparkLot(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        parkingLot park = new parkingLot();
        String selectQuery = "select d.address, d.charges, d.latitude, d.longitude from Parking_lot d where d.name = " +"'" +name +"'" ;
        Cursor cursor = db.rawQuery(selectQuery, null);
       if(cursor.moveToFirst()) {
           do {
               park.setAddress(cursor.getString(0));
               park.setCharges(cursor.getInt(1));
               park.setLatitude(cursor.getFloat(2));
               park.setLongitude(cursor.getFloat(3));
           } while (cursor.moveToNext());
       }
           return park;

    }


    public LinkedList<parkingLot> getParkingLot(Location curlocation) {
        double lati = curlocation.getLatitude();
        double longi = curlocation.getLongitude();
        LinkedList<parkingLot> parklist = new LinkedList<parkingLot>();
        SQLiteDatabase db = this.getWritableDatabase();
       String selectQuery = "select d.name, d.address, d.charges, d.latitude, d.longitude from Parking_lot d where 0.01 > abs(d.latitude - "+ lati +")" +
               " and 0.01 > abs(d.longitude - "+ longi +")";

   //     String selectQuery = "select d.name, d.address, d.charges ,d.latitude, d.longitude  from Parking_lot d";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                parkingLot park = new parkingLot();
                park.setName(cursor.getString(0));
                park.setAddress(cursor.getString(1));
                park.setCharges(cursor.getInt(2));
                park.setLatitude(cursor.getFloat(3));
                park.setLongitude(cursor.getFloat(4));
                parklist.add(park);
            } while (cursor.moveToNext());
        }

        // RETURN THE LIST OF TASKS FROM THE TABLE
        return parklist;
    }



}

package com.example.medicare.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(
        entities = {Medicine.class, User.class},
        version = 3,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MedicineDAO medicineDao();

    public abstract UserDAO userDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `users` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`name` TEXT, " +
                            "`email` TEXT, " +
                            "`username` TEXT, " +
                            "`password` TEXT)"
            );
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL(
                    "CREATE TABLE `users_new` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`name` TEXT, " +
                            "`email` TEXT, " +
                            "`password` TEXT)"
            );

            database.execSQL(
                    "INSERT INTO `users_new` (`id`, `name`, `email`, `password`) " +
                            "SELECT `id`, `name`, `email`, `password` FROM `users`"
            );

            database.execSQL("DROP TABLE `users`");

            database.execSQL(
                    "ALTER TABLE `users_new` RENAME TO `users`"
            );

            database.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS `index_users_email` " +
                            "ON `users` (`email`)"
            );
        }
    };
}
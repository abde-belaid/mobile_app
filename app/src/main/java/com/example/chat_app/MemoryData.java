package com.example.chat_app;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.Context;
import android.content.SharedPreferences;

public class MemoryData {

    private static final String PREF_NAME = "app_memory";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_DATA = "user_data";
    private static final String KEY_LAST_Message = "last_msg_data";


    // Sauvegarder le nom
    public static void saveName(String name, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME, name);
        editor.apply();
    }

    // Récupérer le nom
    public static String getName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_NAME, ""); // Retourne "" si aucune valeur n'est trouvée
    }

    // Sauvegarder des données
    public static void saveData(String data, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_DATA, data);
        editor.apply();
    }

    // Récupérer des données
    public static String getData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_DATA, ""); // Retourne "" si aucune valeur n'est trouvée
    }

    // Sauvegarder des données
    public static void saveLstMsg(String data,String chatId, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(chatId, data);
        editor.apply();
    }

    // Récupérer des données
    public static String getLstMsg(Context context,String chatId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(chatId, "0"); // Retourne "" si aucune valeur n'est trouvée
    }

}


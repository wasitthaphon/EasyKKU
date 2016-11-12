package kku.nukid.wasitthaphon.easykku;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by CSITGIS on 12/11/2559.
 */

public class MyAlert {

    //Explicit
    private Context context;
    private int anInt;
    private String titleString, massageString;

    public MyAlert(Context context, int anInt, String titleString, String massageString) {  //Alt + insert -->generate something
        this.context = context;
        this.anInt = anInt;
        this.titleString = titleString;
        this.massageString = massageString;
    }

    public void myDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setIcon(anInt);
        builder.setTitle(titleString);
        builder.setMessage(massageString);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }   // myDialog



}   // Main Class

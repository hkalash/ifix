package com.smartsoftwaresolutions.ifix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.smartsoftwaresolutions.ifix.map.MapsActivity;

import java.net.URLEncoder;

public class Contact_Us extends AppCompatActivity {
    ImageButton callUsbtn,WhatsUpbtn,btn_browser;
    ImageView iv_send_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__us);
        callUsbtn=findViewById(R.id.callUsbtn1);
        WhatsUpbtn=findViewById(R.id.WhatsUpbtn1);
        btn_browser=findViewById(R.id.btn_browser);
        iv_send_email=findViewById(R.id.iv_send_email1);

        iv_send_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to = "hkhkalash@gmail.com";
                String subject= "Hi subject";
                String body="Hi  message";
                String mailTo = "mailto:" + to +
                        "?&subject=" + Uri.encode(subject) +
                        "&body=" + Uri.encode(body);
                Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                emailIntent.setData(Uri.parse(mailTo));
                startActivity(emailIntent);
            }
        });

        btn_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here i will remove the googel and add the string Web_address



                        String web ="www.siliconlb.com";
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web));
                        startActivity(browserIntent);


            }
        });

        callUsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+"9613102761"));
                if (ActivityCompat.checkSelfPermission(Contact_Us.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });

        WhatsUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smsNumber = "9613102761";
//                try {
//                    Intent sendIntent = new Intent("android.intent.action.MAIN");
//                    sendIntent.setAction(Intent.ACTION_SEND);
//                    sendIntent.setType("text/plain");
//                    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//                    // send data to a pariculer contact
//                    sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net");
//                    sendIntent.setPackage("com.whatsapp");
//                    startActivity(sendIntent);
//                } catch(Exception e) {
//                    //Toast.makeText(Contact_Us.this, "Error\n" + e.toString(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(Worker_Profile.this,e.toString(),Toast.LENGTH_LONG).show();
//                }
                try {

                    PackageManager packageManager = Contact_Us.this.getPackageManager();
                    Intent i = new Intent(Intent.ACTION_VIEW);
// the phone number must be with the country code
                    String url = "https://api.whatsapp.com/send?phone="+ smsNumber +"&text=" + URLEncoder.encode("This is my text to send.", "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        Contact_Us.this.startActivity(i);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



    }
}

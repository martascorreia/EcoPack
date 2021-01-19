package fcul.cm.g20.ecopack.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.View;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class PDF {
    //TODO: refine pdf's creation properties
    public static void create(Uri uri, String text, Context ctx){
        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

        Paint myPaint = new Paint();
        String myString = text;
        int x = 10, y=25;

        for (String line:myString.split("\n")){
            myPage.getCanvas().drawText(line, x, y, myPaint);
            y+=myPaint.descent()-myPaint.ascent();
        }
        myPdfDocument.finishPage(myPage);

        if(uri.getPath() != null) {
            try {
                OutputStream fos = ctx.getContentResolver().openOutputStream(uri);
                myPdfDocument.writeTo(fos);
                Utils.showToast("PDF salvo com sucesso!", ctx);
            } catch (Exception e) {
                e.printStackTrace();
                Utils.showToast("Não foi possível salvar o PDF. Por favor, tente mais tarde.", ctx);
            }
        }
        myPdfDocument.close();
    }
}

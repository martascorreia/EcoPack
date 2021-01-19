package fcul.cm.g20.ecopack.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;

import java.io.OutputStream;

public class PDFPhoto {
    public static void create(Uri uri, String text, Context ctx, Bitmap qr_code){
        PdfDocument myPdfDocument = new PdfDocument();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

        Paint myPaint = new Paint();
        String myString = text;
        int width = myPage.getCanvas().getWidth();
        int height = myPage.getCanvas().getHeight();
        int y=height/3;

        for (String line:myString.split("\n")){
            myPage.getCanvas().drawText(line, width/2 - 30, y, myPaint);
            y+=myPaint.descent()-myPaint.ascent();
        }

        myPage.getCanvas().drawBitmap(qr_code, width/2 - 60, y, myPaint);
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
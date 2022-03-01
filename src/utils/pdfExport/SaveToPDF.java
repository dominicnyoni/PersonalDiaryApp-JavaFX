
package utils.pdfExport;


import Controllers.MainWindow.MainWindowController;
import Controllers.MainWindow.MainWindowController.Page;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.collections.ObservableList;
import utils.Alert.AlertMaker;


public class SaveToPDF {
    


  
   //for saving single diary page as pdf file
   public boolean printToPdf(String[] list,File saveLoc) {
        try {
            if (saveLoc == null) {
                return false;
            }
            if (!saveLoc.getName().endsWith(".pdf")) {
                saveLoc = new File(saveLoc.getAbsolutePath() + ".pdf");
            }
            Document doc=new Document();
            try{
            //Initialize Document
            
            PdfWriter.getInstance(doc, new FileOutputStream(saveLoc));
            
            doc.open();
            
            
            String date=list[0];
            String subject=list[1];
            String content=list[2];
            
            Paragraph para1=new Paragraph(date);
            para1.setAlignment(Element.ALIGN_CENTER);
            para1.setFont(new Font(Font.FontFamily.HELVETICA,24,Font.BOLDITALIC));
            Paragraph para2=new Paragraph(subject);
            para2.setAlignment(Element.ALIGN_CENTER);
            para2.setFont(new Font(Font.FontFamily.HELVETICA,24,Font.BOLDITALIC));
            Paragraph para3=new Paragraph(content);
            para3.setAlignment(Element.BODY);
            para3.setFont(new Font(Font.FontFamily.TIMES_ROMAN,18,Font.NORMAL));
            doc.add(para1);
            doc.add(para2);
            doc.add(para3);
            return true;
            }catch(DocumentException e){
              e.printStackTrace();
            }finally{
                 doc.close();
            }
             
            
            
        }catch (IOException ex) {
            AlertMaker.showErrorMessage("Error occurred during PDF export", ex.getMessage());
        
        
    }
  return false;
 }

//For saving all Diary Pages as one Pdf
public boolean printToPdf(ObservableList<MainWindowController.Page>list,File saveLoc) {
        try {
            if (saveLoc == null) {
                return false;
            }
            if (!saveLoc.getName().endsWith(".pdf")) {
                saveLoc = new File(saveLoc.getAbsolutePath() + ".pdf");
            }
            Document doc=new Document();
            try{
            //Initialize Document
            
            PdfWriter.getInstance(doc, new FileOutputStream(saveLoc));
            
            doc.open();
            for(Page page:list){
            String date=page.getDate();
            String subject=page.getSubject();
            String content=page.getContent();
            
            Paragraph para1=new Paragraph(date);
            para1.setAlignment(Element.ALIGN_CENTER);
            para1.setFont(new Font(Font.FontFamily.HELVETICA,24,Font.BOLDITALIC));
            Paragraph para2=new Paragraph(subject);
            para2.setAlignment(Element.ALIGN_CENTER);
            para2.setFont(new Font(Font.FontFamily.HELVETICA,24,Font.BOLDITALIC));
            Paragraph para3=new Paragraph(content);
            para3.setAlignment(Element.BODY);
            para3.setFont(new Font(Font.FontFamily.TIMES_ROMAN,18,Font.NORMAL));
            doc.add(para1);
            doc.add(para2);
            doc.add(para3);
            }
            return true;
            }catch(DocumentException e){
              e.printStackTrace();
            }finally{
                 doc.close();
            }
             
            
            
        }catch (IOException ex) {
            AlertMaker.showErrorMessage("Error occurred during PDF export", ex.getMessage());
        
        
    }
  return false;
 }
}


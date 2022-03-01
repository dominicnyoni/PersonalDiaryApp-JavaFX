
package model;

public class DiaryPage{
    
   public  DiaryPage(String date, String title, String content){
        setDiary(date,title,content);
    }
   public void setDiary(String date, String title, String content){
        this.date=date;
        this.title=title;
        this.content=content;
       
    }

    public String getDate() {return this.date;}
    public String getTitle() {return this.title;}
    public String getContent() {return this.content;}
  

    private String date;
    private String title;
    private String content;
   
}
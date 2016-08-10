package com.nwt.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmin.
 */
public class MessageBody
{
    private String subject;
    private String header;
    private List<String> paragraphs;

    public MessageBody(String subject, String header)
    {
        setHeader(header);
        setSubject(subject);
        paragraphs = new ArrayList<String>();
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getHeader()
    {
        return header;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }

    public void addParagraph(String paragraph)
    {
        paragraphs.add(paragraph);
    }

    public List<String> getParagraphs()
    {
        return paragraphs;
    }

    public String getContent()
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<h2>" + header + "</h2>");
        for (String paragraph : paragraphs)
        {
            stringBuffer.append("<p>" + paragraph + "</p>");
        }
        return stringBuffer.toString();
    }

    public String getText()
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(header);
        for (String paragraph : paragraphs)
        {
            stringBuffer.append("\n" + paragraph);
        }
        return stringBuffer.toString();
    }
}

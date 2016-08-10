package com.nwt.mail;

/**
 * Created by Jasmin.
 */
public class MessageBodyStyled extends MessageBody
{
    String fileBody;

    public MessageBodyStyled(String subject, String header)
    {
        super(subject, header);
    }

    public String getFileBody()
    {
        return fileBody;
    }

    public void setFileBody(String fileBody)
    {
        this.fileBody = fileBody;
    }

    @Override
    public String getContent()
    {
        return fileBody;
    }
}

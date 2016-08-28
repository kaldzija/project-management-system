package com.nwt.controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.nwt.dao.model.Project;
import com.nwt.dao.model.ProjectFile;
import com.nwt.dao.model.Task;
import com.nwt.dao.model.TaskFile;
import com.nwt.services.ProjectFileService;
import com.nwt.services.ProjectService;
import com.nwt.services.TaskFileService;
import com.nwt.services.TaskService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Jasmin Kaldzija on 19.08.2016..
 */
@RequestMapping(value = "api/files")
@RestController
public class FileController extends BaseController
{
    @Autowired
    TaskService taskService;

    @Autowired
    TaskFileService taskFileService;

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectFileService projectFileService;

    @ResponseBody
    @RequestMapping(value = "/task/{id}", method = RequestMethod.POST)
    public TaskFile uploadTaskFile(@PathVariable(value = "id") Integer taskId, MultipartHttpServletRequest request)
    {
        CommonsMultipartFile multipartFile = (CommonsMultipartFile) ((DefaultMultipartHttpServletRequest) request).getFileMap().get("file");
        Task task = taskService.get(taskId);
        TaskFile taskFile = new TaskFile();
        taskFile.setUser(getCurrentUser());
        taskFile.setCreated(getCurrentTimestamp());
        taskFile.setContent(multipartFile.getBytes());
        taskFile.setFileName(multipartFile.getOriginalFilename());
        taskFile.setSize(multipartFile.getSize());
        taskFile.setContentType(multipartFile.getContentType());
        taskFile.setTask(task);
        taskFileService.saveOrUpdate(taskFile);
        return taskFile;
    }

    @Deprecated
    @RequestMapping(value = "/task/{id}/content", method = RequestMethod.GET)
    public void getTaskContent(@PathVariable(value = "id") Integer id, HttpServletResponse response) throws IOException
    {
        TaskFile taskFile = taskFileService.get(id);
        ByteArrayInputStream in = new ByteArrayInputStream(taskFile.getContent());
        response.setHeader("Content-disposition", "attachment; filename=" + taskFile.getFileName());
        response.setContentType(taskFile.getContentType());
        IOUtils.copy(in, response.getOutputStream());
        in.close();
    }

    @ResponseBody
    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    public List<TaskFile> getTaskFiles(@PathVariable(value = "id") Integer id)
    {
        Task task = taskService.get(id);
        return taskFileService.getTaskFiles(task);
    }

    //Project

    @ResponseBody
    @RequestMapping(value = "/project/{id}", method = RequestMethod.POST)
    public ProjectFile uploadProjectFile(@PathVariable(value = "id") Integer projectId, MultipartHttpServletRequest request)
    {
        CommonsMultipartFile multipartFile = (CommonsMultipartFile) ((DefaultMultipartHttpServletRequest) request).getFileMap().get("file");
        Project project = projectService.get(projectId);
        ProjectFile projectFile = new ProjectFile();
        projectFile.setUser(getCurrentUser());
        projectFile.setCreated(getCurrentTimestamp());
        projectFile.setContent(multipartFile.getBytes());
        projectFile.setFileName(multipartFile.getOriginalFilename());
        projectFile.setSize(multipartFile.getSize());
        projectFile.setContentType(multipartFile.getContentType());
        projectFile.setProject(project);
        projectFileService.saveOrUpdate(projectFile);
        return projectFile;
    }

    @Deprecated
    @RequestMapping(value = "/project/{id}/content", method = RequestMethod.GET)
    public void getProjectFileContent(@PathVariable(value = "id") Integer id, HttpServletResponse response) throws IOException
    {
        ProjectFile projectFile = projectFileService.get(id);
        ByteArrayInputStream in = new ByteArrayInputStream(projectFile.getContent());
        response.setHeader("Content-disposition", "attachment; filename=" + projectFile.getFileName());
        response.setContentType(projectFile.getContentType());
        IOUtils.copy(in, response.getOutputStream());
        in.close();
    }

    @ResponseBody
    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public List<ProjectFile> getProjectFiles(@PathVariable(value = "id") Integer id)
    {
        Project project = projectService.get(id);
        return projectFileService.getProjectFiles(project);
    }

    @Deprecated
    @RequestMapping(value = "/report/{projectId}", method = RequestMethod.GET)
    public void test(@PathVariable(value = "projectId") Integer projectId, HttpServletResponse response) throws IOException
    {
        Project project = projectService.get(projectId);
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy kk:mm");
        try
        {
            @SuppressWarnings("unused") PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
            document.open();

            Font fontHeader1 = new Font(Font.FontFamily.TIMES_ROMAN, 32, Font.BOLD);
            fontHeader1.setColor(24, 166, 137);

            Font fontHeader2 = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);

            Paragraph paragraph = new Paragraph();
            paragraph.setFont(fontHeader1);
            paragraph.add(project.getName());
            paragraph.setAlignment(Element.ALIGN_CENTER);

            Paragraph informationHeader = new Paragraph();
            informationHeader.setFont(fontHeader2);
            informationHeader.add("  1.Details");
            informationHeader.setAlignment(Element.ALIGN_LEFT);

            Paragraph information = new Paragraph();
            information.add("Status: Active");
            information.add(Chunk.NEWLINE);
            information.add("Client: ETF");
            information.add(Chunk.NEWLINE);
            information.add("Version: v1");
            information.add(Chunk.NEWLINE);
            information.add("Created: " + format.format(new Date(project.getCreatedDate().getTime())));
            information.add(Chunk.NEWLINE);
            information.add("Last update:: " + format.format(new Date(project.getUpdated().getTime())));
            information.add(Chunk.NEWLINE);
            information.add("Completed: 45%");

            document.add(paragraph);
            document.add(Chunk.NEWLINE);
            document.add(informationHeader);
            document.add(information);

            Paragraph descHeader = new Paragraph();
            descHeader.setFont(fontHeader2);
            descHeader.add("  2.Description");

            document.add(Chunk.NEWLINE);
            document.add(descHeader);

            Paragraph desc = new Paragraph();
            desc.add(project.getDescription());

            document.add(desc);

            List<Task> tasks = taskService.getProjectTasks(project);
            if (tasks.size() > 0)
            {
                Paragraph tableHeader = new Paragraph();
                tableHeader.setFont(fontHeader2);
                tableHeader.add("  3.Tasks");
                tableHeader.add(Chunk.NEWLINE);
                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);
                PdfPCell c1 = new PdfPCell(new Phrase("Name"));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Status"));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Created by"));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Assigned to"));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Created"));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                c1 = new PdfPCell(new Phrase("Updated"));
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);

                table.setHeaderRows(1);

                for (Task task : tasks)
                {
                    table.addCell(task.getName());
                    table.addCell("To do");
                    table.addCell(task.getCreatedBy().getDisplayName());
                    table.addCell(task.getUser().getDisplayName());
                    table.addCell(format.format(new Date(task.getCreated().getTime())));
                    table.addCell(format.format(new Date(task.getUpdated().getTime())));
                }
                document.add(Chunk.NEWLINE);
                document.add(tableHeader);
                document.add(Chunk.NEWLINE);
                document.add(table);
            }
            document.close();
        } catch (DocumentException e)
        {
            e.printStackTrace();
        }

        ByteArrayInputStream in = new ByteArrayInputStream(outputStream.toByteArray());
        response.setHeader("Content-disposition", "attachment; filename=aaa.pdf");
        response.setContentType("application/pdf");
        IOUtils.copy(in, response.getOutputStream());
        in.close();
    }
}

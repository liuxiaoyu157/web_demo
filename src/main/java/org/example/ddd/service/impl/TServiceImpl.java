package org.example.ddd.service.impl;

import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.example.ddd.service.TService;
import org.example.ddd.util.WordUtil;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TServiceImpl implements TService {

    public static List<String>patternList=new ArrayList();
    //需要处理的节点名称
    static {
        patternList.add("mc:AlternateContent");
        patternList.add("mc:Choice");
        patternList.add("w:drawing");
        patternList.add("wp:anchor");
        patternList.add("a:graphic");
        patternList.add("a:graphicData");
        patternList.add("wps:wsp");
        patternList.add("wps:txbx");
        patternList.add("w:txbxContent");
        patternList.add("w:p");
        patternList.add("w:r");
        patternList.add("w:t");
    }

    @Autowired
    private ApplicationContext applicationContext;
    @Override
    public String readWord1() throws IOException {
        //输入流1
        File file = ResourceUtils.getFile("classpath:u2.docx");
        FileInputStream fileInputStream = new FileInputStream(file);
        //输入流2
        Resource resource = applicationContext.getResource("classpath:model1.docx");
        InputStream inputStream = resource.getInputStream();

        //创建一个word文档对象
        XWPFDocument document = new XWPFDocument(inputStream);

        //创建一个文档执行器
        XWPFWordExtractor extractor = new XWPFWordExtractor(document);

        //获取当前word的文本内容
        String text = extractor.getText();

        //获取当前word的其他属性内容
        POIXMLProperties.CoreProperties coreProperties = extractor.getCoreProperties();
        //获取创建时间
        Date created = coreProperties.getCreated();
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(created);
        System.out.println("createTime = " + createTime);
        //获取创建人
        String creator = coreProperties.getCreator();
        System.out.println("creator = " + creator);



        //关闭流
        fileInputStream.close();
        inputStream.close();
        return text;
    }

    @Override
    public String readWord2() throws IOException, InvalidFormatException {
        //输入流2
        Resource resource = applicationContext.getResource("classpath:ywh_summary_model.docx");
        InputStream inputStream = resource.getInputStream();
        //创建一个word文档对象
        XWPFDocument document = new XWPFDocument(inputStream);

        //遍历节点树
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        //遍历各段落
        /**
         * 注意到我们在遍历文本的时候，循环了两次。
         *
         * 第一次遍历循环 XWPFParagraph 列表，是获取到文档、表格、标题中的段落 XWPFParagraph，这里保存了所有的文本格式信息，但是其实文本内容和更多的格式信息是保存在其子节点 XWPFRun 中的。
         *
         * 第二次遍历 XWPFRun 列表，在这个过程中就可以对文本内容作出处理了。
         */
//        for (int i = 0; i < paragraphs.size(); i++) {
        for (int i = 7; i < 8; i++) {
            System.out.println("paragraphs i = " + i);
            XWPFParagraph paragraph = paragraphs.get(i);
            System.out.println("paragraph = " + paragraph.getText());
            //判断此段落时候需要进行替换
            String text = paragraph.getText();
            System.out.println("text = " + text);
            XWPFRun run = paragraph.getRuns().get(7);
            System.out.println("run = " + run);
            if(StringUtils.isEmpty(text)){
                continue;
            }
            // 文本框中的內容很麻煩
            // 第一步，通过XWPF的XWPFDocument->XWPFParagraph获取XWPFParagraph对象 XWPFParagraph paragraph = paragraphList.get(10);
            // 第二部，获取XWPFParagraph的XmlObject，然后使用XmlObject对象，new一个XmlCursor
//            if (paragraph !=null && paragraph.getCTP() != null && paragraph.getCTP().getRArray(0)!=null){
            if (paragraph !=null && paragraph.getCTP() != null){
                CTP ctp = paragraph.getCTP();
                System.out.println("ctp = " + ctp);

                XmlCursor xmlCursor = ctp.newCursor();
//                List<CTR> ctrList = Arrays.asList(paragraph.getCTP().getRArray());
//                for (int i1 = 0; i1 < ctrList.size(); i1++) {
//
//                    System.out.println("i1 = " + i1);
//                    XmlObject object = ctrList.get(i1);
//                    System.out.println("object = " + object);
//                    XmlCursor cursor = object.newCursor();
////                    cursor.toChild(1);
////                    String textValue = cursor.getTextValue();
////                    System.out.println("textValue = " + textValue);
////                    cursor.setTextValue("唉我去");
//                }

            }





//            System.out.println("NumId" +" ---------------> "+ paragraph.getNumID());
//            System.out.println("Text" +" ---------------> "+ text);
            List<XWPFRun> runs = paragraph.getRuns();
        }

        Map<String, String> replaceMap = new HashMap();

        replaceMap.put("year", String.valueOf(2000));
        replaceMap.put("week", String.valueOf(9));
        replaceMap.put("meetingDate", "2024年09月01日");
        replaceMap.put("meetingPlace", "吉林省白山市等等");
        replaceMap.put("decisionName", "张三,李四,王五,赵六");

        WordUtil.changeText(document, replaceMap);



        // 在新的段落之后添加一个分页符
        XWPFParagraph newParagraph = document.createParagraph();
        newParagraph.setPageBreak(true);
        XWPFRun runTitle = newParagraph.createRun();
        runTitle.setFontSize(16);
        runTitle.setFontFamily("黑体");
        runTitle.setText("会议结论：");

        XWPFParagraph p0 = document.createParagraph();
        XWPFRun runP0 = p0.createRun();
        runP0.setFontSize(16);
        runP0.setFontFamily("黑体");
        runP0.setText("一、这是议题一");

        XWPFParagraph p1 = document.createParagraph();
        XWPFRun runP1 = p1.createRun();
        runP1.setFontSize(16);
        runP1.setFontFamily("仿宋");
        runP1.setText("汇报人：公共事业部 张三");

        XWPFParagraph p2 = document.createParagraph();
        //首行缩进两个字符
        p2.setIndentationFirstLine(400);
        XWPFRun runP2 = p2.createRun();
        runP2.setFontSize(16);
        runP2.setFontFamily("仿宋");
        runP2.setText("1.会议要求");

        XWPFParagraph p3 = document.createParagraph();
        //首行缩进两个字符
        p3.setIndentationFirstLine(400);
        XWPFRun runP3 = p3.createRun();
        runP3.setFontSize(16);
        runP3.setFontFamily("仿宋");
        runP3.setText("(1) 做个锤子。" + " " + "完成时间：2024年2月1日" + " " + "负责人：李四");

        //修改签发人信息
//        document.getParagraphs().get(7).getRuns().get(7).setText("周时莹");
        //签发图片
        XWPFRun run = document.getParagraphs().get(7).getRuns().get(7);
        FileInputStream fileInputStream = new FileInputStream("D:\\sign1.png");
        run.addPicture(fileInputStream, Document.PICTURE_TYPE_PNG, "", Units.toEMU(98), Units.toEMU(22));

//        newParagraph.createRun().setText("Hello, World!");


//创建一个文件对象
        File file = new File("D:\\model11.docx");
        //创建一个文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        //通过文件输出流，将目标文件写入到磁盘
        document.write(fileOutputStream);

        fileOutputStream.close();
        //文件创建成功
        System.out.println("文件创建成功");

        return null;
    }

    @Override
    public String readWord3() throws IOException, XmlException {
        //输入流2
        Resource resource = applicationContext.getResource("classpath:model1.docx");
        InputStream inputStream = resource.getInputStream();
        //创建一个word文档对象
        XWPFDocument document = new XWPFDocument(inputStream);



//        String someWords = "TextBox";
//
//        for (XWPFParagraph paragraph : document.getParagraphs()) {
//            XmlCursor cursor = paragraph.getCTP().newCursor();
//            cursor.selectPath("declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' .//*/w:txbxContent/w:p/w:r");
//
//            List<XmlObject> ctrsintxtbx = new ArrayList<XmlObject>();
//
//            while(cursor.hasNextSelection()) {
//                cursor.toNextSelection();
//                XmlObject obj = cursor.getObject();
//                ctrsintxtbx.add(obj);
//            }
//            for (XmlObject obj : ctrsintxtbx) {
//                CTR ctr = CTR.Factory.parse(obj.xmlText());
//                //CTR ctr = CTR.Factory.parse(obj.newInputStream());
//                XWPFRun bufferrun = new XWPFRun(ctr, (IRunBody)paragraph);
//                String text = bufferrun.getText(0);
//                if (text != null && text.contains(someWords)) {
//                    text = text.replace(someWords, "replaced");
//                    bufferrun.setText(text, 0);
//                }
//                obj.set(bufferrun.getCTR());
//            }
//        }

        File file = new File("D:\\model11.docx");
        //创建一个文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        //通过文件输出流，将目标文件写入到磁盘
        document.write(fileOutputStream);

        fileOutputStream.close();
        //文件创建成功
        System.out.println("文件创建成功");
        return null;
    }

    @Override
    public String readPhoto() throws IOException {
        String filePath = "D:\\";
        //输入流2
        Resource resource = applicationContext.getResource("classpath:u4.docx");
        InputStream inputStream = resource.getInputStream();

        //创建一个word文档对象
        XWPFDocument document = new XWPFDocument(inputStream);

        //准备一个集合，存取所有图片的名字和二进制数据
        HashMap<String, byte[]> images = new HashMap<>();

        //获得所有段落
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            //每一个段落遍历所有区
            List<XWPFRun> runs = paragraph.getRuns();

            for (XWPFRun run : runs) {
                //拿到所有图片
                List<XWPFPicture> pictures = run.getEmbeddedPictures();
                for (XWPFPicture picture : pictures) {
                    //获取文件名
                    String fileName = picture.getPictureData().getFileName();
                    //获取二进制流
                    byte[] data = picture.getPictureData().getData();

                    images.put(fileName, data);
                }
            }
        }

        for (Map.Entry<String, byte[]> entry : images.entrySet()) {
            String fileName = entry.getKey();
            byte[] data = entry.getValue();
            System.out.println("开始写入图片");
            FileOutputStream fileOutputStream = new FileOutputStream(filePath + fileName);
            fileOutputStream.write(data);
            fileOutputStream.close();
            System.out.println("结束写入图片");
        }

        return null;
    }

    @Override
    public String readExcel() throws IOException {
        //输入流2
        Resource resource = applicationContext.getResource("classpath:u3.docx");
        InputStream inputStream = resource.getInputStream();

        //创建一个word文档对象
        XWPFDocument document = new XWPFDocument(inputStream);

        //获取word中所有表格
        List<XWPFTable> tables = document.getTables();
        for (XWPFTable table : tables) {
            List<XWPFTableRow> rows = table.getRows();
            for (XWPFTableRow row : rows) {
                List<XWPFTableCell> cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    String text = cell.getText();
                    System.out.println("text = " + text);
                }
            }
        }
        return null;
    }

    /*private void downloadReportFile(String templatePath, String fileName, Map<String, String> replaceMap, List<String[]> list1, List<String[]> list2, HttpServletResponse response) {
        XWPFDocument document = null;
        InputStream in = null;
        //生成新的word
        response.reset();
        response.setContentType("application/x-msdownload");

        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        OutputStream os = null;

        try {
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));

            in = new ClassPathResource(templatePath).getInputStream();
            //获取docx解析对象
            document = new XWPFDocument(in);
            //解析替换文本段落对象
            WordUtil.changeText(document, replaceMap);
            //解析替换表格对象
            List<XWPFTable> tables = document.getTables();
            if (tables != null && tables.size() > 1) {
                //第一个表格
                WordUtil.changeTable(tables.get(0),list1);
                //第二个表格
                WordUtil.changeTable(tables.get(1), list2);
            }
            os = response.getOutputStream();
            document.write(os);
            os.write(ostream.toByteArray());
            os.close();
            ostream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (document != null) {
                    document.close();
                }
                if (os != null) {
                    os.close();
                }
                if (ostream != null) {
                    ostream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    public static void changeTextBox( XWPFDocument document,Map<String, String>map) {


        for(XWPFParagraph paragraph:document.getParagraphs())
            for(XmlObject object:paragraph.getCTP().getRArray())
            {
                XmlCursor cursor = object.newCursor();
                eachchild(cursor, 0,map);
            }
    }

    public static void eachchild(XmlCursor cursor,int start,Map<String, String>map) {

//预计子节点个数应该是小于10个节点
        for(int i=0;i<10;i++)
        {

//如果可以移动到子节点i
            if(cursor.toChild(i)) {

                //如果移动到达的子节点正好是按照顺序是需要的节点 则继续前往下一层
                if(cursor.getDomNode().getNodeName().equals(patternList.get(start))) {
                    if(start==patternList.size()-1) {
                        String reString=cursor.getTextValue();
                        System.out.println("reString = " + reString);
                        /*for(String e:map.keySet()) {
                            if(reString.contains(e)) {
                                //    执行替换
                                reString=reString.replaceAll(e, map.get(e));
                            }
                        }

                        //bingo  设置替换节点内容
                        cursor.setTextValue(reString);*/
                    }

                    //继续下一层  遍历
                    eachchild(cursor,start+1,map);
                }else {
                    cursor.toParent();
                }
            }
        }

//  此处很重要，如果命中或者未命中都需要 遍历其他节点
        cursor.toParent();
    }


    public static void sayHello() {
        System.out.println("hello");
    }
}

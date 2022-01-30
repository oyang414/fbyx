package com.ouyang.fbyx.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ouyang.fbyx.common.utils.StringUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

/**
 * @description:
 * @author: ouyangxingjie
 * @create: 2021/11/24 18:23
 **/
@RestController
@RequestMapping(value = "/gaode",produces="application/json")
@Validated
@Slf4j
public class GaoDeController {


    @ApiOperation(value="上传excel文件")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void uploadFile (@RequestParam("file") @ApiParam(name="file",value="文件对象") MultipartFile file){
        try {
            InputStream inputStream = file.getInputStream();
            ExcelReader excelReader  = ExcelUtil.getReader(inputStream);
            List<List<Object>> read = excelReader.read(1,excelReader.getRowCount());
            List<ExcelDto> list = new ArrayList<>();
            for(List<Object> objects : read){
                ExcelDto excelDto = new ExcelDto();
                excelDto.setUserName((String)objects.get(0));
                excelDto.setContractNo((String)objects.get(1));
                //excelDto.setSignDate((DateTime)objects.get(2));
                excelDto.setCreditDate((DateTime)objects.get(2));
                excelDto.setLocation((String)objects.get(3));
                String dis = String.valueOf(objects.get(4));
                if(StringUtil.isBlank(dis)){
                    String startLonLat = getLonLat("湖北省武汉市");
                    String endLonLat = getLonLat(excelDto.getLocation());
                    BigDecimal distance = getDistance(startLonLat,endLonLat);
                    BigDecimal doubleDis = distance.divide(new BigDecimal("1000"),2,BigDecimal.ROUND_HALF_UP);
                    excelDto.setDistance(String.valueOf(doubleDis.doubleValue()));
                }else {
                    excelDto.setDistance(dis);
                }
                list.add(excelDto);
            }
            ExcelWriter writer = ExcelUtil.getWriter();
            //构造表格头
            writer.addHeaderAlias("userName", "客户名称");
            writer.addHeaderAlias("contractNo", "合同号");
            //writer.addHeaderAlias("signDate", "协议签订日期");
            writer.addHeaderAlias("creditDate", "放款日期");
            writer.addHeaderAlias("location", "居住地");
            writer.addHeaderAlias("distance", "公里数");
            writer.addHeaderAlias("area", "区域");
            writer.write(list, true);
            //构造excel的文件名称
            FileOutputStream out = new FileOutputStream("D:\\222.xls");
            writer.flush(out, true);

        } catch (IOException e) {
            log.error("上传文件异常：{}",e);
        }

    }


        public static void main(String[] args) throws IOException{
            FileInputStream in = new FileInputStream("D:\\test.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int dataLength = sheet.getLastRowNum() - 1;
            log.info("总共{}条数据...",dataLength);
            for(int i = 1;i <= dataLength;i++) {
                XSSFRow row = sheet.getRow(i);
                //获取居住地所在的单元格
                Cell cell = row.getCell(3);
                //设置开始位置
                String startLonLat = getLonLat("湖北省武汉市");
                //设置结束位置（居住地）
                String endLonLat = getLonLat(cell.getStringCellValue());
                //请求高德地图API获取开始位置到结束位置的距离
                BigDecimal distance = getDistance(startLonLat, endLonLat);
                //换算距离单位为：千米（公里），并保留两位小数
                BigDecimal doubleDis = distance.divide(new BigDecimal("1000"), 2, BigDecimal.ROUND_HALF_UP);
                //获取公里数所在单元格
                Cell disCell = row.getCell(4);
                //将距离数值写入到公里数所在的单元格内
                disCell.setCellValue(String.valueOf(doubleDis.doubleValue()));
            }
            //最后将处理的结果写入文件中
            FileOutputStream out = new FileOutputStream("D:\\test.xlsx");
            try {
                out.flush();
                workbook.write(out);
                out.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private static String getLonLat(String address){
            //返回输入地址address的经纬度信息, 格式是 经度,纬度
            String queryUrl = "http://restapi.amap.com/v3/geocode/geo?key=5dda595e4649b7d685d4e2144872f2da&address="+address;
            String queryResult = getResponse(queryUrl);  //高德接品返回的是JSON格式的字符串
            System.out.println("queryResult:"+queryResult);
            JSONObject jo = JSONObject.parseObject(queryResult);
            JSONArray ja = jo.getJSONArray("geocodes");
            return JSONObject.parseObject(ja.getString(0)).get("location").toString();
        }

        private static BigDecimal getDistance(String startLonLat, String endLonLat){
            //返回起始地startAddr与目的地endAddr之间的距离，单位：米
            BigDecimal result = new BigDecimal(0);
            String queryUrl = "http://restapi.amap.com/v3/distance?key=5dda595e4649b7d685d4e2144872f2da&origins="+startLonLat+"&destination="+endLonLat;
            String queryResult = getResponse(queryUrl);
            System.out.println("queryResult:" + queryResult);
            JSONObject jo = JSONObject.parseObject(queryResult);
            JSONArray ja = jo.getJSONArray("results");
            result = new BigDecimal(JSONObject.parseObject(ja.getString(0)).get("distance").toString());
            return result;
        }

        private static String getResponse(String serverUrl){
            //用JAVA发起http请求，并返回json格式的结果
            StringBuffer result = new StringBuffer();
            try {
                URL url = new URL(serverUrl);
                URLConnection conn = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;
                while((line = in.readLine()) != null){
                    result.append(line);
                }
                in.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
        }


}

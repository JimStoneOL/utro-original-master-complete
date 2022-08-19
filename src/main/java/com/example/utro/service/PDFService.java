package com.example.utro.service;

import com.example.utro.entity.ClothWarehouse;
import com.example.utro.entity.FurnitureWarehouse;
import com.example.utro.entity.ImageModel;
import com.example.utro.entity.Order;
import com.example.utro.payload.other.CustomMap;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class PDFService {
    private final ClothWarehouseService clothWarehouseService;
    private final FurnitureWarehouseService furnitureWarehouseService;
    private final ImageUploadService imageUploadService;
    private final OrderService orderService;

    @Autowired
    public PDFService(ClothWarehouseService clothWarehouseService, FurnitureWarehouseService furnitureWarehouseService, ImageUploadService imageUploadService, OrderService orderService) {
        this.clothWarehouseService = clothWarehouseService;
        this.furnitureWarehouseService = furnitureWarehouseService;
        this.imageUploadService = imageUploadService;
        this.orderService = orderService;
    }


    public void remains(HttpServletResponse response) throws IOException {
        List<CustomMap> input=remainsData();
        export(response,input);
    }
    public void moves(HttpServletResponse response) throws IOException {
        List<CustomMap> input=movesData();
        export(response,input);
    }
    private void export(HttpServletResponse response,List<CustomMap> input) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter writer=PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        ImageModel imageModel=imageUploadService.getImageToOther("utro");
        Image image=Image.getInstance(imageModel.getImageBytes());
        document.add(image);

        writeContent(input,document);
        document.close();
    }
    private void writeContent(List<CustomMap> input, Document document){
        for(int i=0;i<input.size();i++){
            Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA, StandardCharsets.UTF_8.name());
            Paragraph paragraph=new Paragraph(input.get(i).getKey()+" : "+input.get(i).getRes(),fontParagraph);
            paragraph.setAlignment(Paragraph.ALIGN_LEFT);
            document.add(paragraph);
        }
    }
    private List<CustomMap> remainsData(){
        List<CustomMap> results=new ArrayList<>();
        List<ClothWarehouse> clothWarehouse=clothWarehouseService.getAllClothWarehouses();
        CustomMap title=new CustomMap("title","Remains");
        CustomMap space=new CustomMap("-----------------","-----------------");
        CustomMap underTitleCloth=new CustomMap("emphasize","Cloth");
        results.add(title);
        results.add(space);
        results.add(underTitleCloth);
        for(int i=0;i<clothWarehouse.size();i++){
            CustomMap customMap=new CustomMap(clothWarehouse.get(i).getId().toString(),clothWarehouse.get(i).getLength().toString());
            results.add(customMap);
        }
        results.add(space);
        List<FurnitureWarehouse> furnitureWarehouses=furnitureWarehouseService.getAllFurnitureWarehouses();
        CustomMap underTitleFurniture=new CustomMap("emphasize","Furniture");
        results.add(underTitleFurniture);
        for(int i=0;i<furnitureWarehouses.size();i++){
            String amount= String.valueOf(furnitureWarehouses.get(i).getAmount());
            CustomMap customMap=new CustomMap(furnitureWarehouses.get(i).getId().toString(),amount);
            results.add(customMap);
        }
        return results;
    }
    private List<CustomMap> movesData(){
        List<CustomMap> results=new ArrayList<>();
        List<Order> orders=orderService.getAllOrders();
        CustomMap title=new CustomMap("title","Moves");
        CustomMap space=new CustomMap("-----------------","-----------------");
        results.add(title);
        results.add(space);
        for(int i=0;i<orders.size();i++){
            Order order=orders.get(i);
            CustomMap customMap=new CustomMap(order.getId().toString(),order.getStage().toString());
            results.add(customMap);
        }
        return results;
    }
}
package by.i4t.test;

import by.i4t.objects.Specialty;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class SpecialtyChanger {

    public static void main(String[] argv) {

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://192.168.10.10:5432/edu_organizations", "postgres","root");

            ArrayList<Specialty> specialtyList = new ArrayList<Specialty>();

            Statement forSpecilty = connection.createStatement();
            Statement forSearchName = connection.createStatement();
            Statement forVuzDoc = connection.createStatement();

            String cleanQ = "update specialty_catalog set qualification=null where qualification is not null";
            query(connection, cleanQ);//очистка поля квалификация

            String selectSpecilty = "select * from specialty_catalog";
            ResultSet rs = forSpecilty.executeQuery(selectSpecilty);

            //выгружаем список специальностей
            while (rs.next()){
                UUID idSpecialty = UUID.fromString(rs.getString("id"));
                String okrb_code = rs.getString("okrb_code");
                String code = null;
                String name = rs.getString("name");

                Specialty specialty = new Specialty(idSpecialty, okrb_code, code, name);
                specialtyList.add(specialty);
            }

            //ищем те, у которых больше 10 знаков
            for (Specialty specialty: specialtyList) {
                if (specialty.okrb_code.length()>10 & specialty.okrb_code.substring(0,2).equals("1-")){
                    if (!specialty.okrb_code.substring(0,5).equals("1-100")) {
                        specialty.code = specialty.okrb_code.substring(0, 10);
                    }else  if (specialty.okrb_code.length()>11 & specialty.okrb_code.substring(0,5).equals("1-100")){
                        specialty.code = specialty.okrb_code.substring(0, 11);
                    }
                }
            }

            //выгружаем список документов
            ArrayList<VuzDoc> vuzDocList = new ArrayList<VuzDoc>();
            String selectVuzDoc = "select * from vuz_doc";
            ResultSet rsVuzDoc = forVuzDoc.executeQuery(selectVuzDoc);
            while (rsVuzDoc.next()){
                UUID vuzdocID = UUID.fromString(rsVuzDoc.getString("id"));
                UUID vuzdocSpecialty = UUID.fromString(rsVuzDoc.getString("specialty"));
                String vuzdocSpecialization = rsVuzDoc.getString("specialization_txt");

                VuzDoc vuzDoc = new VuzDoc(vuzdocID, vuzdocSpecialty, vuzdocSpecialization);
                vuzDocList.add(vuzDoc);
            }

            //обновляем VuzDoc и удаляем специальность
            for (Specialty specialty : specialtyList){
                if (specialty.code!=null){
                    try{
                        //ищем главную специальность
                        String findNameSpec = "select * from specialty_catalog where okrb_code='" + specialty.code + "'";
                        ResultSet resultSet = forSearchName.executeQuery(findNameSpec);

                        update:
                        while (resultSet.next()){
                            UUID higherId = UUID.fromString(resultSet.getString("id"));

                            //обновляем specialization_txt у всех документов
                            for (VuzDoc vuzDoc : vuzDocList){
                                if (vuzDoc.specialty.equals(specialty.id)){
                                    vuzDoc.specialization = specialty.name;
                                    vuzDoc.specialty = higherId;

                                    String updateVuzDoc = "update vuz_doc set specialty ='" + vuzDoc.specialty +
                                            "', specialization='" + vuzDoc.specialization +
                                            "', status=2" +
                                            " where id = '" + vuzDoc.id + "'";
                                    query(connection, updateVuzDoc);
                                }
                            }

                            String updateSpecialty = "delete from specialty_catalog where id = '" + specialty.id + "'" ;
                            query(connection, updateSpecialty);
                            System.out.println("Удалена специальность " + specialty.name);

                            break update;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static void query(Connection connection, String query){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static class Specialty{
        UUID id;
        String okrb_code;
        String code;
        String name;

        Specialty(UUID id, String okrb_code, String code, String name){
            this.id = id;
            this.okrb_code = okrb_code;
            this.code = code;
            this.name = name;

        }
    }

    static class VuzDoc{
        UUID id;
        UUID specialty;
        String specialization;

        VuzDoc(UUID id, UUID specialty, String specialization){
            this.id = id;
            this.specialty = specialty;
            this.specialization = specialization;
        }
    }

}

package com.globits.hr.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import com.globits.core.dto.CountryDto;
import com.globits.core.dto.DepartmentDto;
import com.globits.core.dto.EthnicsDto;
import com.globits.core.dto.PersonAddressDto;
import com.globits.core.dto.ReligionDto;
import com.globits.hr.dto.HRDepartmentDto;
import com.globits.hr.dto.HrSpecialityDto;
import com.globits.hr.dto.PersonCertificateDto;
import com.globits.hr.dto.PositionStaffDto;
import com.globits.hr.dto.PositionTitleDto;
import com.globits.hr.dto.StaffInsuranceHistoryDto;
import com.globits.hr.dto.StaffDto;
import com.globits.hr.dto.StaffEducationHistoryDto;
import com.globits.hr.dto.StaffLabourAgreementDto;
import com.globits.hr.dto.StaffSalaryHistoryDto;
import com.globits.hr.dto.function.ImportExcelMessageDto;
import com.globits.hr.dto.function.ImportStaffDto;
import com.globits.hr.dto.function.PositionTitleStaffDto;
import com.globits.hr.dto.function.StaffFamilyRelationshipFunctionDto;
import com.globits.security.dto.UserDto;

@Component
public class ImportExportExcelUtil {
    private static Hashtable<String, Integer> hashStaffColumnConfig = new Hashtable<>();
    private static Hashtable<String, Integer> hashDepartmentColumnConfig = new Hashtable<>();
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static DecimalFormat numberFormatter = new DecimalFormat("######################");
    private static Hashtable<String, String> hashColumnPropertyConfig = new Hashtable<>();

    private static void scanStaffColumnExcelIndex(Sheet datatypeSheet, int scanRowIndex) {
        Row row = datatypeSheet.getRow(scanRowIndex);
        int numberCell = row.getPhysicalNumberOfCells();

        hashColumnPropertyConfig.put("staffCode".toLowerCase(), "staffCode");
        hashColumnPropertyConfig.put("firstName".toLowerCase(), "firstName");
        hashColumnPropertyConfig.put("lastName".toLowerCase(), "lastName");
        hashColumnPropertyConfig.put("displayName".toLowerCase(), "displayName");
        hashColumnPropertyConfig.put("birthdate".toLowerCase(), "birthdate");
        hashColumnPropertyConfig.put("birthdateMale".toLowerCase(), "birthdateMale");
        hashColumnPropertyConfig.put("birthdateFeMale".toLowerCase(), "birthdateFeMale");
        hashColumnPropertyConfig.put("gender".toLowerCase(), "gender");
        hashColumnPropertyConfig.put("address".toLowerCase(), "address");// Cái này cần xem lại
        hashColumnPropertyConfig.put("userName".toLowerCase(), "userName");
        hashColumnPropertyConfig.put("password".toLowerCase(), "password");
        hashColumnPropertyConfig.put("email".toLowerCase(), "email");
        hashColumnPropertyConfig.put("BirthPlace".toLowerCase(), "BirthPlace");

        hashColumnPropertyConfig.put("departmentCode".toLowerCase(), "departmentCode");
        hashColumnPropertyConfig.put("MaNgach".toLowerCase(), "MaNgach");
        hashColumnPropertyConfig.put("IDCard".toLowerCase(), "IDCard");

        for (int i = 0; i < numberCell; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellTypeEnum() == CellType.STRING) {
                String cellValue = cell.getStringCellValue();
                if (cellValue != null && cellValue.length() > 0) {
                    cellValue = cellValue.toLowerCase().trim();
                    String propertyName = hashColumnPropertyConfig.get(cellValue);
                    if (propertyName != null) {
                        hashStaffColumnConfig.put(propertyName, i);
                    }
                }
            }
        }
    }

    public static List<DepartmentDto> getListDepartmentFromInputStream(InputStream is) {
        try {
            List<DepartmentDto> ret = new ArrayList<>();
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            int rowIndex = 4;

            hashDepartmentColumnConfig.put("code", 0);
            hashDepartmentColumnConfig.put("name", 1);

            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell = null;
                if (currentRow != null) {
                    DepartmentDto department = new DepartmentDto();
                    Integer index = hashDepartmentColumnConfig.get("code");
                    if (index != null) {
                        currentCell = currentRow.getCell(index);// code
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            double value = currentCell.getNumericCellValue();
                            String code = numberFormatter.format(value);
                            department.setCode(code);
                        } else if (currentCell != null && currentCell.getStringCellValue() != null) {
                            String code = currentCell.getStringCellValue();
                            department.setCode(code);
                        }
                    }
                    index = hashDepartmentColumnConfig.get("name");
                    if (index != null) {
                        currentCell = currentRow.getCell(index);// name
                        if (currentCell != null && currentCell.getStringCellValue() != null) {
                            String name = currentCell.getStringCellValue();
                            department.setName(name);
                        }
                    }
                    ret.add(department);
                }
                rowIndex++;
            }
            return ret;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<StaffDto> getListStaffFromInputStream(InputStream is) {
        try {
            List<StaffDto> ret = new ArrayList<>();
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            int rowIndex = 1;
            int scanRowIndex = 0;
            scanStaffColumnExcelIndex(datatypeSheet, scanRowIndex);

            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                System.out.println("rowIndex=" + rowIndex);
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell;
                if (currentRow != null) {
                    StaffDto staff = new StaffDto();
                    Integer index = hashStaffColumnConfig.get("staffCode");
                    if (index != null) {
                        currentCell = currentRow.getCell(index);// staffCode
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            double value = currentCell.getNumericCellValue();
                            String staffCode = numberFormatter.format(value);
                            staff.setStaffCode(staffCode);
                        } else if (currentCell != null && currentCell.getStringCellValue() != null) {
                            String staffCode = currentCell.getStringCellValue();
                            if (staffCode != null) {
                                staffCode = staffCode.trim();
                            }
                            staff.setStaffCode(staffCode);
                        }
                    }
                    if (staff.getStaffCode() != null && staff.getStaffCode().length() > 0) {
                        index = hashStaffColumnConfig.get("firstName");
                        if (index != null) {
                            currentCell = currentRow.getCell(index);// firstName
                            if (currentCell != null && currentCell.getStringCellValue() != null) {
                                String firstName = currentCell.getStringCellValue();
                                staff.setFirstName(firstName);
                            }
                        }
                        index = hashStaffColumnConfig.get("lastName");
                        if (index != null) {
                            currentCell = currentRow.getCell(index);// lastName
                            if (currentCell != null && currentCell.getStringCellValue() != null) {
                                String lastName = currentCell.getStringCellValue();
                                staff.setLastName(lastName);
                            }
                        }
                        index = hashStaffColumnConfig.get("displayName");
                        if (index != null) {
                            currentCell = currentRow.getCell(index);// lastName
                            if (currentCell != null && currentCell.getStringCellValue() != null) {
                                String displayName = currentCell.getStringCellValue();
                                staff.setDisplayName(displayName);
                                if (staff.getFirstName() == null) {
                                    int lastIndex = displayName.lastIndexOf(' ');
                                    if (lastIndex > 0 && lastIndex < displayName.length()) {
                                        int endIndex = displayName.length();
                                        String firstName = displayName.substring(lastIndex, endIndex);
                                        String lastName = displayName.substring(0, lastIndex);
                                        staff.setFirstName(firstName);
                                        staff.setLastName(lastName);
                                    }
                                }
                            }
                        }
                        index = hashStaffColumnConfig.get("birthdateMale");
                        if (index != null) {
                            currentCell = currentRow.getCell(index);// birthdate
                            if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                    && currentCell.getStringCellValue() != null) {
                                String strBirthdate = currentCell.getStringCellValue();
                                if (strBirthdate != null) {
                                    try {
                                        Date birthDate = dateFormat.parse(strBirthdate);
                                        staff.setBirthDate(birthDate);
                                        staff.setGender("M");
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                                double dateValue = currentCell.getNumericCellValue();
                                if (dateValue > 0) {
                                    try {
                                        Date birthDate = new Date(Math.round(dateValue));
                                        staff.setBirthDate(birthDate);
                                        staff.setGender("F");
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                        index = hashStaffColumnConfig.get("birthdateFeMale");
                        if (index != null) {
                            currentCell = currentRow.getCell(index);// birthdate
                            if (currentCell != null && currentCell.getStringCellValue() != null
                                    && currentCell.getCellTypeEnum() == CellType.STRING) {
                                String strBirthdate = currentCell.getStringCellValue();
                                if (strBirthdate != null) {
                                    try {
                                        Date birthDate = dateFormat.parse(strBirthdate);
                                        staff.setBirthDate(birthDate);
                                        staff.setGender("M");
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                                double dateValue = currentCell.getNumericCellValue();
                                if (dateValue > 0) {
                                    try {
                                        Date birthDate = new Date(Math.round(dateValue));
                                        staff.setBirthDate(birthDate);
                                        staff.setGender("M");
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                        index = hashStaffColumnConfig.get("birthdate");
                        if (index != null) {
                            currentCell = currentRow.getCell(index);// birthdate
                            if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                    && currentCell.getStringCellValue() != null) {
                                String strBirthdate = currentCell.getStringCellValue();
                                if (strBirthdate != null) {
                                    try {
                                        Date birthDate = dateFormat.parse(strBirthdate);
                                        staff.setBirthDate(birthDate);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                                double dateValue = currentCell.getNumericCellValue();
                                if (dateValue > 0) {
                                    try {
                                        Date birthDate = new Date(Math.round(dateValue));
                                        staff.setBirthDate(birthDate);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                        index = hashStaffColumnConfig.get("gender");
                        if (index != null) {
                            currentCell = currentRow.getCell(index);// gender
                            if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                    && currentCell.getStringCellValue() != null) {
                                String gender = currentCell.getStringCellValue();
                                if (gender.equals("0")) {
                                    staff.setGender("M");
                                } else if (gender.equals("1")) {
                                    staff.setGender("F");
                                } else {
                                    staff.setGender("U");
                                }
                            } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                                int dateValue = (int) currentCell.getNumericCellValue();
                                if (dateValue == 0) {
                                    staff.setGender("M");
                                } else if (dateValue == 1) {
                                    staff.setGender("F");
                                } else {
                                    staff.setGender("U");
                                }
                            }
                        }

                        index = hashStaffColumnConfig.get("IDCard");// cmnd
                        if (index != null) {
                            currentCell = currentRow.getCell(index);// cmnd

                            if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                    && currentCell.getStringCellValue() != null) {
                                String cmnd = currentCell.getStringCellValue();
                                if (cmnd != null) {
                                    staff.setIdNumber(cmnd);
                                }
                            } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                                double value = currentCell.getNumericCellValue();
                                String cmnd = numberFormatter.format(value);
                                if (cmnd != null) {
                                    staff.setIdNumber(cmnd);
                                }
                            }
                        }

                        index = hashStaffColumnConfig.get("userName"); // create userName nếu có
                        if (index != null) {
                            currentCell = currentRow.getCell(index);// userName
                            String userName = null;
                            if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING) {
                                userName = currentCell.getStringCellValue();
                            } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                                userName = "" + currentCell.getNumericCellValue();
                            }
                            if (userName != null) {
                                UserDto user = new UserDto();
                                user.setUsername(userName);
                                index = hashStaffColumnConfig.get("password");
                                if (index != null) {
                                    currentCell = currentRow.getCell(index);// password
                                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                                        double dPassword = currentCell.getNumericCellValue();
                                        String password = numberFormatter.format(dPassword);
                                        user.setPassword(password);
                                    } else if (currentCell != null && currentCell.getStringCellValue() != null) {
                                        String password = currentCell.getStringCellValue();
                                        user.setPassword(password);
                                    }
                                } else {
                                    user.setPassword("123456");
                                }
                                index = hashStaffColumnConfig.get("email");
                                if (index != null) {
                                    currentCell = currentRow.getCell(index);// email
                                    if (currentCell != null && currentCell.getStringCellValue() != null) {
                                        String email = currentCell.getStringCellValue();
                                        user.setEmail(email);
                                        staff.setUser(user);
                                    }
                                } else {
                                    String email = staff.getStaffCode() + "@tlu.edu.vn";
                                    user.setEmail(email);
                                    staff.setUser(user);
                                }
                            } else if (staff.getStaffCode() != null) {// lấy tạm userName là staffCode
                                UserDto user = new UserDto();
                                user.setUsername(staff.getStaffCode());
                                index = hashStaffColumnConfig.get("password");
                                if (index != null) {
                                    currentCell = currentRow.getCell(index);// password
                                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                                        double dPassword = currentCell.getNumericCellValue();
                                        String password = numberFormatter.format(dPassword);
                                        user.setPassword(password);
                                    } else if (currentCell != null && currentCell.getStringCellValue() != null) {
                                        String password = currentCell.getStringCellValue();
                                        user.setPassword(password);
                                    }
                                } else if (staff.getIdNumber() != null) {
                                    user.setPassword(staff.getIdNumber());
                                } else {
                                    user.setPassword("123456");
                                }
                                index = hashStaffColumnConfig.get("email");
                                if (index != null) {
                                    currentCell = currentRow.getCell(index);// email
                                    if (currentCell != null && currentCell.getStringCellValue() != null) {
                                        String email = currentCell.getStringCellValue();
                                        user.setEmail(email);
                                        staff.setUser(user);
                                    }
                                } else {
                                    String email = staff.getStaffCode() + "@tlu.edu.vn";
                                    user.setEmail(email);
                                    staff.setUser(user);
                                }
                            }
                        } else if (staff.getStaffCode() != null) {// lấy tạm userName là staffCode
                            UserDto user = new UserDto();
                            user.setUsername(staff.getStaffCode());
                            index = hashStaffColumnConfig.get("password");
                            if (index != null) {
                                currentCell = currentRow.getCell(index);// password
                                if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                                    double dPassword = currentCell.getNumericCellValue();
                                    String password = numberFormatter.format(dPassword);
                                    user.setPassword(password);
                                } else if (currentCell != null && currentCell.getStringCellValue() != null) {
                                    String password = currentCell.getStringCellValue();
                                    user.setPassword(password);
                                }
                            } else if (staff.getIdNumber() != null) {
                                user.setPassword(staff.getIdNumber());
                            } else {
                                user.setPassword("123456");
                            }
                            index = hashStaffColumnConfig.get("email");
                            if (index != null) {
                                currentCell = currentRow.getCell(index);// email
                                if (currentCell != null && currentCell.getStringCellValue() != null) {
                                    String email = currentCell.getStringCellValue();
                                    user.setEmail(email);
                                    staff.setUser(user);
                                }
                            } else {
                                String email = staff.getStaffCode() + "@tlu.edu.vn";
                                user.setEmail(email);
                                staff.setUser(user);
                            }
                        }
                        index = hashStaffColumnConfig.get("departmentCode");
                        if (index != null) {
                            currentCell = currentRow.getCell(index);// userName
                            if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING) {
                                String departmentCode = currentCell.getStringCellValue();
                                PositionStaffDto position = new PositionStaffDto();
                                position.setCurrent(true);
                                position.setDepartment(new HRDepartmentDto());
                                position.getDepartment().setCode(departmentCode);
                                position.setMainPosition(true);
                                position.setPosition(new PositionTitleDto());
                                staff.getPositions().add(position);
                            }
                        }

                        index = hashStaffColumnConfig.get("BirthPlace");
                        if (index != null) {
                            currentCell = currentRow.getCell(index);// birthPlace
                            if (currentCell != null && currentCell.getStringCellValue() != null) {
                                String birthPlace = currentCell.getStringCellValue();
                                staff.setBirthPlace(birthPlace);
                            }
                        }
                        index = hashStaffColumnConfig.get("ethnic");
                        if (index != null) {
                            currentCell = currentRow.getCell(index);// ethnic
                            if (currentCell != null && currentCell.getStringCellValue() != null) {
                                String ethnicCode = currentCell.getStringCellValue();
                                EthnicsDto ethnicsDto = new EthnicsDto();
                                ethnicsDto.setCode(ethnicCode);
                                staff.setEthnics(ethnicsDto);
                            }
                        }
                        index = hashStaffColumnConfig.get("religion");
                        if (index != null) {
                            currentCell = currentRow.getCell(index);// religion
                            if (currentCell != null && currentCell.getStringCellValue() != null) {
                                String religionCode = currentCell.getStringCellValue();
                                ReligionDto religionDto = new ReligionDto();
                                religionDto.setCode(religionCode);
                                staff.setReligion(religionDto);
                            }
                        }
                        index = hashStaffColumnConfig.get("phoneNumber");
                        if (index != null) {
                            currentCell = currentRow.getCell(index);// phoneNumber
                            if (currentCell != null && currentCell.getStringCellValue() != null) {
                                String phoneNumber = currentCell.getStringCellValue();
                                staff.setPhoneNumber(phoneNumber);
                            }
                        }
//						index = hashStaffColumnConfig.get("MaNgach");
//						if (index != null) {
//							currentCell = currentRow.getCell(index);// MaNgach
//							if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
//								double value = currentCell.getNumericCellValue();
//								String code = numberFormatter.format(value);
//								
//							} else if (currentCell != null && currentCell.getStringCellValue() != null) {
//								String code = currentCell.getStringCellValue();
//								if(code!=null) {
//									
//								}
//								
//							}
//						}

                        ret.add(staff);
                    }
                }
                rowIndex++;
            }
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ByteArrayResource exportStaffToExcelTable(List<StaffDto> dataList, Boolean isCheck)
            throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet1");

        /* Tạo font */
        XSSFFont fontBold = workbook.createFont();
        fontBold.setBold(true); // set bold
        fontBold.setFontHeight(10); // add font size

        XSSFFont fontBoldTitle = workbook.createFont();
        fontBoldTitle.setBold(true); // set bold
        fontBoldTitle.setFontHeight(15); // add font size

        /* Tạo cell style */
        XSSFCellStyle titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCellStyle.setFont(fontBoldTitle);

        XSSFCellStyle tableHeadCellStyle = workbook.createCellStyle();
        tableHeadCellStyle.setFont(fontBold);
        tableHeadCellStyle.setBorderBottom(BorderStyle.THIN);
        tableHeadCellStyle.setBorderTop(BorderStyle.THIN);
        tableHeadCellStyle.setBorderLeft(BorderStyle.THIN);
        tableHeadCellStyle.setBorderRight(BorderStyle.THIN);

        XSSFRow row = sheet.createRow(0);
        XSSFCell cell;

        cell = row.createCell(0);
        cell.setCellValue("Mã viên chức");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(1);
        cell.setCellValue("Họ và tên");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(2);
        cell.setCellValue("Ngày sinh");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(3);
        cell.setCellValue("Giới tính");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(4);
        cell.setCellValue("Trình độ");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(5);
        cell.setCellValue("Loại viên chức");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(6);
        cell.setCellValue("Mã nghạch ");
        cell.setCellStyle(tableHeadCellStyle);

        cell = row.createCell(7);
        cell.setCellValue("Quê quán");
        cell.setCellStyle(tableHeadCellStyle);

//		if(isCheck) {
//			cell = row.createCell(4);
//			cell.setCellValue("Tên người nhận");
//			cell.setCellStyle(tableHeadCellStyle);
//			
//			cell = row.createCell(5);
//			cell.setCellValue("Số điện thoại");
//			cell.setCellStyle(tableHeadCellStyle);
//			
//			cell = row.createCell(6);
//			cell.setCellValue("Email");
//			cell.setCellStyle(tableHeadCellStyle);
//			
//			cell = row.createCell(7);
//			cell.setCellValue("Kết quả");
//			cell.setCellStyle(tableHeadCellStyle);
//			
//			cell = row.createCell(8);
//			cell.setCellValue("Trạng thái vận chuyển mẫu chính");
//			cell.setCellStyle(tableHeadCellStyle);
//			
//			cell = row.createCell(9);
//			cell.setCellValue("Bộ mấu đối chiếu");
//			cell.setCellStyle(tableHeadCellStyle);
//		}
        // Tạo các hàng cột dữ liệu
        XSSFRow tableDataRow;
        if (dataList != null && !dataList.isEmpty()) {
            for (int i = 0; i < dataList.size(); i++) {
                String domicile = "";
                String qualification = "";
                String civilServantCategory = "";
                String civilServantType = "";
                tableDataRow = sheet.createRow(i + 1);
                StaffDto data = dataList.get(i);

                // Dữ liệu kết quả
                if (data != null) {
                    if (data.getQualification() == null) {
                        qualification += "";
                    } else {
                        qualification += data.getQualification();
                    }

                }
                if (data != null) {
                    if (data.getCivilServantType() == null) {
                        civilServantType += "";
                    } else {
                        civilServantType += data.getCivilServantType().getName();
                    }

                }
                if (data != null) {
                    if (data.getCivilServantCategory() == null) {
                        civilServantCategory += "";
                    } else {
                        civilServantCategory += data.getCivilServantCategory().getName();
                    }

                }
                if (data != null) {
                    if (data.getAddress() == null) {
                        domicile += "";
                    } else {
                        if (data.getAddress().size() > 0) {
                            boolean isCheckAdd = false;
                            for (PersonAddressDto dto : data.getAddress()) {
                                if (dto.getType() == 3) {
                                    isCheckAdd = true;
                                    domicile = dto.getAddress();
                                }
                            }
                            if (!isCheckAdd) {
                                domicile += "";
                            }
                        }
                    }
                }
                if (data != null) {
                    tableDataRow.createCell(0).setCellValue(data.getStaffCode());
                    tableDataRow.createCell(1).setCellValue(data.getDisplayName());
                    tableDataRow.createCell(2).setCellValue(data.getBirthDate());
                    tableDataRow.createCell(3).setCellValue(data.getGender());
                    tableDataRow.createCell(4).setCellValue(qualification);
                    tableDataRow.createCell(5).setCellValue(civilServantType);
                    tableDataRow.createCell(6).setCellValue(civilServantCategory);
                    tableDataRow.createCell(7).setCellValue(domicile);
                }
                sheet.autoSizeColumn(i);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            out.close();
            return new ByteArrayResource(out.toByteArray());
        }
        return null;
    }

    public static ImportStaffDto importStaffFromInputStream(InputStream is) {
        ImportStaffDto importStaffDto = new ImportStaffDto();
        List<StaffDto> staff = new ArrayList<>();
        List<ImportExcelMessageDto> listMessage = new ArrayList<>();
        try {
            // cảnh báo
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            int rowIndex = 1;
            int num = datatypeSheet.getLastRowNum();
            Calendar calendar = Calendar.getInstance();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell;
                ImportExcelMessageDto message = new ImportExcelMessageDto();
                if (currentRow != null) {
                    StaffDto dto = new StaffDto();

                    // firstName
                    Integer index = 0;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String firstName = String.valueOf(currentCell.getNumericCellValue());
                        dto.setFirstName(firstName);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String firstName = currentCell.getStringCellValue().trim();
                        dto.setFirstName(firstName);
                    }
                    // lastname
                    index = 1;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String lastname = String.valueOf(currentCell.getNumericCellValue());
                        dto.setLastName(lastname);
                        System.out.println(lastname);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String lastname = currentCell.getStringCellValue().trim();
                        dto.setLastName(lastname);
                        System.out.println(lastname);
                    }
                    // displayName
                    index = 2;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String displayName = String.valueOf(currentCell.getNumericCellValue());
                        dto.setDisplayName(displayName);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String displayName = currentCell.getStringCellValue().trim();
                        dto.setDisplayName(displayName);
                    }
                    // gender
                    index = 3;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String gender = String.valueOf((int) currentCell.getNumericCellValue());
                        if (gender.equals("1")) {
                            dto.setGender("F");
                        } else if (gender.equals("0")) {
                            dto.setGender("M");
                        } else {
                            dto.setGender("U");
                        }
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String gender = currentCell.getStringCellValue().trim();
                        if (gender.equals("1")) {
                            dto.setGender("F");
                        } else if (gender.equals("0")) {
                            dto.setGender("M");
                        } else {
                            dto.setGender("U");
                        }
                    }
                    //
                    index = 4;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
                            calendar.setTime(currentCell.getDateCellValue());
                            dto.setBirthDate(calendar.getTime());
                        }
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
                            calendar.setTime(currentCell.getDateCellValue());
                            dto.setBirthDate(calendar.getTime());
                        }
                    }
                    // birthPlace
                    index = 5;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String birthPlace = String.valueOf(currentCell.getNumericCellValue());
                        dto.setBirthPlace(birthPlace);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String birthPlace = currentCell.getStringCellValue().trim();
                        dto.setBirthPlace(birthPlace);
                        System.out.println(birthPlace);
                    }

                    Set<PersonAddressDto> listAdd = new HashSet<>();
                    index = 6;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String address = String.valueOf(currentCell.getNumericCellValue());
                        System.out.println(address);
                        PersonAddressDto personAddressDto = new PersonAddressDto();
                        personAddressDto.setAddress(address);
                        personAddressDto.setType(1);
                        listAdd.add(personAddressDto);
                        dto.setAddress(listAdd);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        PersonAddressDto personAddressDto = new PersonAddressDto();
                        String address = String.valueOf(currentCell.getStringCellValue());
                        System.out.println(address);
                        personAddressDto.setAddress(address);
                        personAddressDto.setType(1);
                        listAdd.add(personAddressDto);
                        dto.setAddress(listAdd);
                    }
                    // Address
                    index = 7;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String address = String.valueOf(currentCell.getNumericCellValue());
                        PersonAddressDto personAddressDto = new PersonAddressDto();
                        personAddressDto.setAddress(address);
                        personAddressDto.setType(2);
                        listAdd.add(personAddressDto);
                        dto.setAddress(listAdd);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String address = String.valueOf(currentCell.getStringCellValue());
                        PersonAddressDto personAddressDto = new PersonAddressDto();
                        personAddressDto.setAddress(address);
                        personAddressDto.setType(2);
                        listAdd.add(personAddressDto);
                        dto.setAddress(listAdd);
                    }
                    // NativePlace
                    index = 8;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String address = String.valueOf(currentCell.getNumericCellValue());
                        PersonAddressDto personAddressDto = new PersonAddressDto();
                        personAddressDto.setAddress(address);
                        personAddressDto.setType(3);
                        listAdd.add(personAddressDto);
                        dto.setAddress(listAdd);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String address = String.valueOf(currentCell.getStringCellValue());
                        PersonAddressDto personAddressDto = new PersonAddressDto();
                        personAddressDto.setAddress(address);
                        personAddressDto.setType(3);
                        listAdd.add(personAddressDto);
                        dto.setAddress(listAdd);
                    }
                    // IDCard
                    index = 9;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String iDCard = String.valueOf((long) currentCell.getNumericCellValue());
                        dto.setIdNumber(iDCard);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String iDCard = String.valueOf(currentCell.getStringCellValue());
                        dto.setIdNumber(iDCard);
                    }
                    // NgayCapCMT
                    index = 10;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
                            calendar.setTime(currentCell.getDateCellValue());
                            dto.setIdNumberIssueDate(calendar.getTime());
                        }

                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
                            calendar.setTime(currentCell.getDateCellValue());
                            dto.setIdNumberIssueDate(calendar.getTime());
                        }
                    }
                    // NoiCapCMT
                    index = 11;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String idNumberIssueBy = String.valueOf(currentCell.getNumericCellValue());
                        dto.setIdNumberIssueBy(idNumberIssueBy);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String idNumberIssueBy = String.valueOf(currentCell.getStringCellValue());
                        dto.setIdNumberIssueBy(idNumberIssueBy);
                    }
                    // nationality
                    index = 12;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String nationality = String.valueOf(currentCell.getNumericCellValue());
                        dto.setNationalityCode(nationality);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String nationality = String.valueOf(currentCell.getStringCellValue());
                        dto.setNationalityCode(nationality);
                    }
                    // ethnics
                    index = 13;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String ethnics = String.valueOf(currentCell.getNumericCellValue());
                        dto.setEthnicsCode(ethnics);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String ethnics = String.valueOf(currentCell.getStringCellValue());
                        dto.setEthnicsCode(ethnics);
                    }
                    // religion
                    index = 14;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String religion = String.valueOf(currentCell.getNumericCellValue());
                        dto.setReligionCode(religion);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String religion = String.valueOf(currentCell.getStringCellValue());
                        dto.setReligionCode(religion);
                    }
                    // Email
                    index = 15;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String email = String.valueOf(currentCell.getNumericCellValue());
                        dto.setEmail(email);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String email = String.valueOf(currentCell.getStringCellValue());
                        dto.setEmail(email);
                    }
                    // Mobile
                    index = 16;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String mobile = String.valueOf((long) currentCell.getNumericCellValue());
                        dto.setPhoneNumber(mobile);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String mobile = String.valueOf(currentCell.getStringCellValue());
                        dto.setPhoneNumber(mobile);
                    }
                    // tinhtranghonnhan
                    index = 17;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String maritalStatus = String.valueOf(currentCell.getNumericCellValue());
//						dto.setMaritalStatus();
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String maritalStatus = String.valueOf(currentCell.getStringCellValue());
//						dto.setMaritalStatus(maritalStatus);
                    }
                    // trangThaiNhanVien
                    index = 18;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String status = String.valueOf(currentCell.getNumericCellValue());
                        dto.setStatusCode(status);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String status = String.valueOf(currentCell.getStringCellValue());
                        dto.setStatusCode(status);
                    }
                    // maPhongBan
                    index = 19;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String department = String.valueOf((int) currentCell.getNumericCellValue());
                        dto.setDepartmentCode(department);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String department = currentCell.getStringCellValue().trim();
                        dto.setDepartmentCode(department);
                    }
                    // maNhanVien
                    index = 20;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String staffCode = String.valueOf(currentCell.getNumericCellValue());
                        dto.setStaffCode(staffCode);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String staffCode = currentCell.getStringCellValue().trim();
                        dto.setStaffCode(staffCode);
                    } else if (currentCell == null) {
                        message.setIndex(rowIndex + "");
                        message.setMessage("Không có mã nhân viên");
                    }
                    // LoaiVienChuc
                    index = 21;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String civilServantTypeCode = String.valueOf(currentCell.getNumericCellValue());
                        dto.setCivilServantTypeCode(civilServantTypeCode);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String civilServantTypeCode = String.valueOf(currentCell.getStringCellValue());
                        dto.setCivilServantTypeCode(civilServantTypeCode);
                    }
                    // Ngachcongchuc
                    index = 22;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String civilServantCategoryCode = String.valueOf(currentCell.getNumericCellValue());
                        dto.setCivilServantCategoryCode(civilServantCategoryCode);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String civilServantCategoryCode = String.valueOf(currentCell.getStringCellValue());
                        dto.setCivilServantCategoryCode(civilServantCategoryCode);
                    }
                    // loaiHopDong
                    index = 23;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String labourAgreementTypeCode = String.valueOf(currentCell.getNumericCellValue());
                        dto.setLabourAgreementTypeCode(labourAgreementTypeCode);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String labourAgreementTypeCode = String.valueOf(currentCell.getStringCellValue());
                        dto.setLabourAgreementTypeCode(labourAgreementTypeCode);
                    }
                    // ngayhopdong
                    index = 24;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
                            calendar.setTime(currentCell.getDateCellValue());
                            dto.setContractDate(calendar.getTime());
                        }
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
                            calendar.setTime(currentCell.getDateCellValue());
                            dto.setContractDate(calendar.getTime());
                        }
                    }
                    // NgayTuyenDung
                    index = 25;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
                            calendar.setTime(currentCell.getDateCellValue());
                            dto.setRecruitmentDate(calendar.getTime());
                        }

                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
                            calendar.setTime(currentCell.getDateCellValue());
                            dto.setRecruitmentDate(calendar.getTime());
                        }
                    }
                    // chucDanhChuyenMon
                    index = 26;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String professionalTitles = String.valueOf(currentCell.getNumericCellValue());
                        dto.setProfessionalTitles(professionalTitles);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String professionalTitles = String.valueOf(currentCell.getStringCellValue());
                        dto.setProfessionalTitles(professionalTitles);
                    }
                    // congViecDuocGiao
                    index = 27;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String profession = String.valueOf(currentCell.getNumericCellValue());
                        dto.setProfessionCode(profession);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String profession = String.valueOf(currentCell.getStringCellValue());
                        dto.setProfessionCode(profession);
                    }
                    // BacLuong
                    index = 28;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String salaryLeve = String.valueOf(currentCell.getNumericCellValue());
                        dto.setSalaryLeve(salaryLeve);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String salaryLeve = String.valueOf(currentCell.getStringCellValue());
                        dto.setSalaryLeve(salaryLeve);
                    }
                    // HeSoLuong
                    index = 29;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String salaryCoefficient = String.valueOf(currentCell.getNumericCellValue());
                        dto.setSalaryCoefficient(salaryCoefficient);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String salaryCoefficient = String.valueOf(currentCell.getStringCellValue());
                        dto.setSalaryCoefficient(salaryCoefficient);
                    }
                    // NgayHuongBacLuong
                    index = 30;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
                            calendar.setTime(currentCell.getDateCellValue());
                            dto.setSalaryStartDate(calendar.getTime());
                        }
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
                            calendar.setTime(currentCell.getDateCellValue());
                            dto.setSalaryStartDate(calendar.getTime());
                        }
                    }
                    // SoSoBaoHiemXaHoi
                    index = 31;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String socialInsuranceNumber = String.valueOf(currentCell.getNumericCellValue());
                        dto.setSocialInsuranceNumber(socialInsuranceNumber);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String socialInsuranceNumber = String.valueOf(currentCell.getStringCellValue());
                        dto.setSocialInsuranceNumber(socialInsuranceNumber);
                    }
                    // Học hàm
                    index = 32;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String academicTitleCode = String.valueOf(currentCell.getNumericCellValue());
                        dto.setAcademicTitleCode(academicTitleCode);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String academicTitleCode = currentCell.getStringCellValue().trim();
                        dto.setAcademicTitleCode(academicTitleCode);
                    }
                    // Học vị
                    index = 33;
                    currentCell = currentRow.getCell(index);
                    if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                        String educationDegreeCode = String.valueOf(currentCell.getNumericCellValue());
                        dto.setEducationDegreeCode(educationDegreeCode);
                    } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                            && currentCell.getStringCellValue() != null) {
                        String educationDegreeCode = currentCell.getStringCellValue().trim();
                        dto.setEducationDegreeCode(educationDegreeCode.trim());
                    }
                    staff.add(dto);
                    if (message.getIndex() != null) {
                        listMessage.add(message);
                    }
                }
                rowIndex++;
            }
            if (listMessage.size() > 0) {
                importStaffDto.setListMessage(listMessage);
            }
            importStaffDto.setListStaff(staff);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return importStaffDto;
    }

    public static List<PositionTitleStaffDto> importPositionProcessFromInputStream(InputStream is) {
        List<PositionTitleStaffDto> listData = new ArrayList<>();
        try {
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            int rowIndex = 1;
            Calendar calendar = Calendar.getInstance();
            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell;
                if (currentRow != null) {
                    PositionTitleStaffDto dto = new PositionTitleStaffDto();
                    Integer index = 0;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String staffCode = String.valueOf(currentCell.getNumericCellValue());
                            dto.setStaffCode(staffCode);
                            System.out.println(staffCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String staffCode = currentCell.getStringCellValue().trim();
                            dto.setStaffCode(staffCode);
                            System.out.println(staffCode);
                        }
                    }

                    index = 1;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String potitionTitleCode = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setPositionTitleCode(potitionTitleCode);
                            System.out.println(potitionTitleCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String potitionTitleCode = currentCell.getStringCellValue().trim();
                            dto.setPositionTitleCode(potitionTitleCode);
                            System.out.println(potitionTitleCode);
                        }
                    }
                    index = 2;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String decisionCode = String.valueOf(currentCell.getNumericCellValue());
                            dto.setDecisionCode(decisionCode);
                            System.out.println(decisionCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String decisionCode = currentCell.getStringCellValue().trim();
                            dto.setDecisionCode(decisionCode);
                            System.out.println(decisionCode);
                        }
                    }
                    index = 3;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String allowanceCoefficient = String.valueOf(currentCell.getNumericCellValue());
                            dto.setAllowanceCoefficient(currentCell.getNumericCellValue());
                            System.out.println(allowanceCoefficient);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String allowanceCoefficient = currentCell.getStringCellValue().trim();
                            dto.setAllowanceCoefficient(Double.valueOf(allowanceCoefficient));
                            System.out.println(allowanceCoefficient);
                        }
                    }
                    index = 4;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String current = String.valueOf(currentCell.getNumericCellValue());
                            if (current.equals("1.0")) {
                                dto.setCurrent(true);
                            }
                            if (current.equals("0.0")) {
                                dto.setCurrent(false);
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String current = currentCell.getStringCellValue().trim();
                            if (current.equals("1")) {
                                dto.setCurrent(true);
                            }
                            if (current.equals("0")) {
                                dto.setCurrent(false);
                            }
                        }
                    }
                    index = 5;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String fromDateString;
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setFromDate(calendar.getTime());
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            fromDateString = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(fromDateString);
                                dto.setFromDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }
                    index = 6;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String toDateString;
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setToDate(calendar.getTime());
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            toDateString = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(toDateString);
                                dto.setToDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }

                    index = 7;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String departmentStr = String.valueOf(currentCell.getNumericCellValue());
                            dto.setDepartmentStr(departmentStr);
                            System.out.println(departmentStr);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String departmentStr = currentCell.getStringCellValue().trim();
                            dto.setDepartmentStr(departmentStr);
                            System.out.println(departmentStr);
                        }
                    }
                    index = 8;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String note = String.valueOf(currentCell.getNumericCellValue());
                            dto.setNote(note);
                            System.out.println(note);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String note = currentCell.getStringCellValue().trim();
                            dto.setNote(note);
                            System.out.println(note);
                        }
                    }
                    index = 9;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String decisionDateString;
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setDecisionDate(calendar.getTime());
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            decisionDateString = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(decisionDateString);
                                dto.setDecisionDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }
                    index = 10;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String connectedAllowanceProcess = String.valueOf(currentCell.getNumericCellValue());
                            if (connectedAllowanceProcess.equals("1.0")) {
                                dto.setConnectedAllowanceProcess(true);
                            }
                            if (connectedAllowanceProcess.equals("0.0")) {
                                dto.setConnectedAllowanceProcess(false);
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String connectedAllowanceProcess = currentCell.getStringCellValue().trim();
                            if (connectedAllowanceProcess.equals("1")) {
                                dto.setConnectedAllowanceProcess(true);
                            }
                            if (connectedAllowanceProcess.equals("0")) {
                                dto.setConnectedAllowanceProcess(false);
                            }
                        }
                    }
                    index = 11;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String mainPosition = String.valueOf(currentCell.getNumericCellValue());
                            if (mainPosition.equals("1.0")) {
                                dto.setMainPosition(true);
                            }
                            if (mainPosition.equals("0.0")) {
                                dto.setMainPosition(false);
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String mainPosition = currentCell.getStringCellValue().trim();
                            if (mainPosition.equals("1")) {
                                dto.setMainPosition(true);
                            }
                            if (mainPosition.equals("0")) {
                                dto.setMainPosition(false);
                            }
                        }
                    }
                    index = 12;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String department = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setDepartmentCode(department);
                            System.out.println(department);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String department = currentCell.getStringCellValue().trim();
                            dto.setDepartmentCode(department);
                            System.out.println(department);
                        }
                    }
                    listData.add(dto);
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listData;
    }

    public static List<StaffFamilyRelationshipFunctionDto> importStaffFamilyRelationshipProcessFromInputStream(
            InputStream is) {
        List<StaffFamilyRelationshipFunctionDto> listData = new ArrayList<>();
        try {
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            int rowIndex = 1;
            Calendar calendar = Calendar.getInstance();
            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell = null;
                if (currentRow != null) {
                    StaffFamilyRelationshipFunctionDto dto = new StaffFamilyRelationshipFunctionDto();

                    Integer index = 0;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String staffCode = String.valueOf(currentCell.getNumericCellValue());
                            dto.setStaffCode(staffCode);
                            System.out.println(staffCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String staffCode = currentCell.getStringCellValue().trim();
                            dto.setStaffCode(staffCode);
                            System.out.println(staffCode);
                        }
                    }

                    index = 1;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String fullName = String.valueOf(currentCell.getNumericCellValue());
                            dto.setFullName(fullName);
                            System.out.println(fullName);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String fullName = currentCell.getStringCellValue().trim();
                            dto.setFullName(fullName);
                            System.out.println(fullName);
                        }
                    }
                    index = 2;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String address = String.valueOf(currentCell.getNumericCellValue());
                            dto.setAddress(address);
                            System.out.println(address);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String address = currentCell.getStringCellValue().trim();
                            dto.setAddress(address);
                            System.out.println(address);
                        }
                    }
                    index = 3;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setBirthDate(calendar.getTime());
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setBirthDate(calendar.getTime());
                            }
                        }
                    }
                    index = 4;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String familyRelationship = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setFamilyRelationship(familyRelationship);
                            System.out.println(familyRelationship);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String familyRelationship = currentCell.getStringCellValue().trim();
                            dto.setFamilyRelationship(familyRelationship);
                            System.out.println(familyRelationship);
                        }
                    }
                    listData.add(dto);
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listData;
    }

    public static List<CountryDto> importCountryFromInputStream(InputStream is) {
        List<CountryDto> listData = new ArrayList<>();
        try {
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            int rowIndex = 1;
            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell = null;
                if (currentRow != null) {
                    CountryDto dto = new CountryDto();

                    Integer index = 0;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String name = String.valueOf(currentCell.getNumericCellValue());
                            dto.setName(name);
                            System.out.println(name);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String name = currentCell.getStringCellValue().trim();
                            dto.setName(name);
                            System.out.println(name);
                        }
                    }
                    index = 1;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String code = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setCode(code);
                            System.out.println(code);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String code = currentCell.getStringCellValue().trim();
                            dto.setCode(code);
                            System.out.println(code);
                        }
                    }
                    listData.add(dto);
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listData;
    }

    public static List<StaffSalaryHistoryDto> importStaffSalaryHistoryFromInputStream(InputStream is) {
        List<StaffSalaryHistoryDto> listData = new ArrayList<>();
        try {
            // cảnh báo
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            int rowIndex = 1;
            Calendar calendar = Calendar.getInstance();
            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell;
                if (currentRow != null) {
                    StaffSalaryHistoryDto dto = new StaffSalaryHistoryDto();

                    Integer index = 0;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String staffCode = String.valueOf(currentCell.getNumericCellValue());
                            dto.setStaffCode(staffCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String staffCode = currentCell.getStringCellValue().trim();
                            dto.setStaffCode(staffCode);
                        }
                    }

                    index = 1;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            Double coefficient = Double.parseDouble(String.valueOf(currentCell.getNumericCellValue()));
                            dto.setCoefficient(coefficient);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String[] splits = currentCell.getStringCellValue().trim().split(",");
                            double coefficient;
                            if (splits.length > 1) {
                                String s = splits[0] + "." + splits[1];
                                coefficient = Double.parseDouble(s);
                            } else {
                                coefficient = Double.parseDouble(currentCell.getStringCellValue().trim());
                            }
                            dto.setCoefficient(coefficient);
                        }
                    }

                    index = 2;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String staffTypeCode = String.valueOf(currentCell.getNumericCellValue());
                            dto.setStaffTypeCode(staffTypeCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String staffTypeCode = currentCell.getStringCellValue().trim();
                            dto.setStaffTypeCode(staffTypeCode);
                        }
                    }

                    index = 3;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            Double coefficientOverLevel = Double
                                    .parseDouble(String.valueOf(currentCell.getNumericCellValue()));
                            dto.setCoefficientOverLevel(coefficientOverLevel);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            Double coefficientOverLevel = Double.parseDouble(currentCell.getStringCellValue().trim());
                            dto.setCoefficientOverLevel(coefficientOverLevel);
                        }
                    }

                    index = 4;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            Double percentage = Double.parseDouble(String.valueOf(currentCell.getNumericCellValue()));
                            dto.setPercentage(percentage);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            Double percentage = Double.parseDouble(currentCell.getStringCellValue().trim());
                            dto.setPercentage(percentage);
                        }
                    }

                    index = 5;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String decisionDateString;
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setDecisionDate(calendar.getTime());
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            decisionDateString = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(decisionDateString);
                                dto.setDecisionDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }

                    index = 6;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String decisionCode = String.valueOf(currentCell.getNumericCellValue());
                            dto.setDecisionCode(decisionCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String decisionCode = currentCell.getStringCellValue().trim();
                            dto.setDecisionCode(decisionCode);
                        }
                    }

                    index = 7;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String salaryIncrementTypeCode = String.valueOf(currentCell.getNumericCellValue());
                            dto.setSalaryIncrementTypeCode(salaryIncrementTypeCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String salaryIncrementTypeCode = currentCell.getStringCellValue().trim();
                            dto.setSalaryIncrementTypeCode(salaryIncrementTypeCode);
                        }
                    }

                    index = 8;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String decisionDateString;
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setStartDate(calendar.getTime());
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            decisionDateString = currentCell.getStringCellValue();
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(decisionDateString);
                                dto.setStartDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }

                    index = 9;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String decisionDateString;

                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setStartDate(calendar.getTime());
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            decisionDateString = currentCell.getStringCellValue();
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(decisionDateString);
                                dto.setNextSalaryIncrementDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }

                    index = 10;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String note = String.valueOf(currentCell.getNumericCellValue());
                            dto.setNote(note);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String note = currentCell.getStringCellValue().trim();
                            dto.setNote(note);
                        }
                    }

                    index = 11;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String decisionDateString;
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setStartDate(calendar.getTime());
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            decisionDateString = currentCell.getStringCellValue();
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(decisionDateString);
                                dto.setStartStaffTypeCodeDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }
                    listData.add(dto);
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listData;
    }

    public static List<StaffEducationHistoryDto> importStaffEducationHistoryFromInputStream(InputStream is) {
        List<StaffEducationHistoryDto> listData = new ArrayList<>();
        try {
            // cảnh báo
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            int rowIndex = 1;
            Calendar calendar = Calendar.getInstance();
            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell;
                if (currentRow != null) {
                    StaffEducationHistoryDto dto = new StaffEducationHistoryDto();

                    Integer index = 0;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String staffCode = String.valueOf(currentCell.getNumericCellValue());
                            dto.setStaffCode(staffCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String staffCode = currentCell.getStringCellValue().trim();
                            dto.setStaffCode(staffCode);
                        }
                    }
                    index = 2;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String majorCode = String.valueOf(currentCell.getNumericCellValue());
                            dto.setMajorCode(majorCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String majorCode = currentCell.getStringCellValue().trim();
                            dto.setMajorCode(majorCode);
                        }
                    }
                    index = 3;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String educationTypeCode = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setEducationTypeCode(educationTypeCode);
                            System.out.println(educationTypeCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String educationTypeCode = currentCell.getStringCellValue().trim();
                            dto.setEducationTypeCode(educationTypeCode);
                            System.out.println(educationTypeCode);
                        }
                    }
                    index = 5;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String countryCode = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setCountryCode(countryCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String countryCode = String.valueOf(currentCell.getStringCellValue().trim());
                            dto.setCountryCode(countryCode);
                        }
                    }

                    index = 1;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String specialityCode = String.valueOf(currentCell.getNumericCellValue());
                            dto.setSpecialityCode(specialityCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String specialityCode = String.valueOf(currentCell.getStringCellValue().trim());
                            dto.setSpecialityCode(specialityCode);
                        }
                    }

                    index = 4;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String educationDegreeCode = String.valueOf(currentCell.getNumericCellValue());
                            dto.setEducationDegreeCode(educationDegreeCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String educationDegreeCode = String.valueOf(currentCell.getStringCellValue().trim());
                            dto.setEducationDegreeCode(educationDegreeCode);
                        }
                    }

                    index = 6;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String startDate;
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setStartDate(calendar.getTime());
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            startDate = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(startDate);
                                dto.setStartDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }
                    //
                    index = 7;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String endDate;

                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setEndDate(calendar.getTime());
                            }

                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            endDate = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(endDate);
                                dto.setEndDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }
                    // schoolName
                    index = 9;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String schoolName = String.valueOf(currentCell.getNumericCellValue());
                            dto.setSchoolName(schoolName);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String schoolName = currentCell.getStringCellValue().trim();
                            dto.setSchoolName(schoolName);
                        }
                    }
                    // Place
                    index = 8;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String place = String.valueOf(currentCell.getNumericCellValue());
                            dto.setPlace(place);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String place = currentCell.getStringCellValue().trim();
                            dto.setPlace(place);
                        }
                    }
                    //IsCurrent
                    index = 10;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String isCurrent = String.valueOf(currentCell.getNumericCellValue());
                            if (isCurrent.equals("1.0")) {
                                dto.setIsCurrent(true);
                            }
                            if (isCurrent.equals("0.0")) {
                                dto.setIsCurrent(false);
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String isCurrent = currentCell.getStringCellValue().trim();
                            if (isCurrent.equals("1")) {
                                dto.setIsCurrent(true);
                            }
                            if (isCurrent.equals("0")) {
                                dto.setIsCurrent(false);
                            }
                        }
                    }
                    //SoQuyetDinh
                    index = 11;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String decisionCode = String.valueOf(currentCell.getNumericCellValue());
                            dto.setDecisionCode(decisionCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String decisionCode = currentCell.getStringCellValue().trim();
                            dto.setDecisionCode(decisionCode);
                        }
                    }
                    // NguonKinhPhi
                    index = 12;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String fundingSource = String.valueOf(currentCell.getNumericCellValue());
                            dto.setFundingSource(fundingSource);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String fundingSource = currentCell.getStringCellValue().trim();
                            dto.setFundingSource(fundingSource);
                        }
                    }
                    //Ghi chú
                    index = 13;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String note = String.valueOf(currentCell.getNumericCellValue());
                            dto.setNote(note);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String note = currentCell.getStringCellValue().trim();
                            dto.setNote(note);
                        }
                    }
                    //Xác nhận
                    index = 14;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String isConfirmation = String.valueOf(currentCell.getNumericCellValue());
                            if (isConfirmation.equals("1.0")) {
                                dto.setIsConfirmation(true);
                            }
                            if (isConfirmation.equals("0.0")) {
                                dto.setIsConfirmation(false);
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String isConfirmation = currentCell.getStringCellValue().trim();
                            if (isConfirmation.equals("1")) {
                                dto.setIsConfirmation(true);
                            }
                            if (isConfirmation.equals("0")) {
                                dto.setIsConfirmation(false);
                            }
                        }
                    }
                    // Được tính thâm niên
                    index = 15;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String isCountedForSeniority = String.valueOf(currentCell.getNumericCellValue());
                            if (isCountedForSeniority.equals("1.0")) {
                                dto.setIsCountedForSeniority(true);
                            }
                            if (isCountedForSeniority.equals("0.0")) {
                                dto.setIsCountedForSeniority(false);
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String isCountedForSeniority = currentCell.getStringCellValue().trim();
                            if (isCountedForSeniority.equals("1")) {
                                dto.setIsCountedForSeniority(true);
                            }
                            if (isCountedForSeniority.equals("0")) {
                                dto.setIsCountedForSeniority(false);
                            }
                        }
                    }

                    //Căn cứ basis
                    index = 16;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String basis = String.valueOf(currentCell.getNumericCellValue());
                            dto.setBasis(basis);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String basis = currentCell.getStringCellValue().trim();
                            dto.setBasis(basis);
                        }
                    }
                    //NgayQuyetDinh
                    index = 17;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String decisionDate;
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setDecisionDate(calendar.getTime());
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            decisionDate = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(decisionDate);
                                dto.setDecisionDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }
                    //NamTiepNhanVe
                    index = 18;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String returnDate;
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setReturnDate(calendar.getTime());
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            returnDate = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(returnDate);
                                dto.setReturnDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }
                    //NotFinish
                    index = 19;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String notFinish = String.valueOf(currentCell.getNumericCellValue());
                            if (notFinish.equals("1.0")) {
                                dto.setNotFinish(true);
                            }
                            if (notFinish.equals("0.0")) {
                                dto.setNotFinish(false);
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String isCountedForSeniority = currentCell.getStringCellValue().trim();
                            if (isCountedForSeniority.equals("1")) {
                                dto.setNotFinish(true);
                            }
                            if (isCountedForSeniority.equals("0")) {
                                dto.setNotFinish(false);
                            }
                        }
                    }
                    //NgayKetThucTheoQD
                    index = 20;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String finishDateByDecision;
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setFinishDateByDecision(calendar.getTime());
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            finishDateByDecision = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(finishDateByDecision);
                                dto.setFinishDateByDecision(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }
                    //GiaHan
                    index = 21;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String extendDateByDecision;

                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setExtendDateByDecision(calendar.getTime());
                            }

                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            extendDateByDecision = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(extendDateByDecision);
                                dto.setExtendDateByDecision(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }
                    //SoQĐGiaHan
                    index = 22;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String extendDecisionCode = String.valueOf(currentCell.getNumericCellValue());
                            dto.setExtendDecisionCode(extendDecisionCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String extendDecisionCode = currentCell.getStringCellValue().trim();
                            dto.setExtendDecisionCode(extendDecisionCode);
                        }
                    }
                    //
                    index = 23;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String isExtended = String.valueOf(currentCell.getNumericCellValue());
                            if (isExtended.equals("1.0")) {
                                dto.setIsExtended(true);
                            }
                            if (isExtended.equals("0.0")) {
                                dto.setIsExtended(false);
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String isExtended = currentCell.getStringCellValue().trim();
                            if (isExtended.equals("1")) {
                                dto.setIsExtended(true);
                            }
                            if (isExtended.equals("0")) {
                                dto.setIsExtended(false);
                            }
                        }
                    }
                    //NgayQuyetDinhGiaHan
                    index = 24;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String extendDecisionDate;

                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setExtendDecisionDate(calendar.getTime());
                            }

                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            extendDecisionDate = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(extendDecisionDate);
                                dto.setExtendDecisionDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }
                    listData.add(dto);
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listData;
    }


    public static List<HrSpecialityDto> importHrSpecialityFromInputStream(InputStream is) {
        List<HrSpecialityDto> listData = new ArrayList<>();
        try {
            // cảnh báo
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            int rowIndex = 1;
            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell;
                if (currentRow != null) {
                    HrSpecialityDto dto = new HrSpecialityDto();

                    Integer index = 1;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String name = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setName(name);
                            System.out.println(name);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String name = currentCell.getStringCellValue().trim();
                            dto.setName(name);
                            System.out.println(name);
                        }
                    }
                    index = 0;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String code = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setCode(code);
                            System.out.println(code);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String code = currentCell.getStringCellValue().trim();
                            dto.setCode(code);
                            System.out.println(code);
                        }
                    }
                    listData.add(dto);
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listData;
    }

    public static List<StaffInsuranceHistoryDto> importExcelStaffInsuranceHistoryDto(InputStream is) {
        List<StaffInsuranceHistoryDto> listData = new ArrayList<>();
        try {
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            int rowIndex = 1;
            Calendar calendar = Calendar.getInstance();
            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell;
                if (currentRow != null) {
                    StaffInsuranceHistoryDto dto = new StaffInsuranceHistoryDto();

                    Integer index = 0;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String staffCode = String.valueOf(currentCell.getNumericCellValue());
                            StaffDto staffDto = new StaffDto();
                            staffDto.setStaffCode(staffCode);
                            dto.setStaff(staffDto);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String staffCode = currentCell.getStringCellValue().trim();
                            StaffDto staffDto = new StaffDto();
                            staffDto.setStaffCode(staffCode);
                            dto.setStaff(staffDto);
                        }

                    }

                    index = 1;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String startDate;
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setStartDate(calendar.getTime());
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            startDate = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(startDate);
                                dto.setStartDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }

                    index = 2;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String endDate;
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setEndDate(calendar.getTime());
                            }

                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            endDate = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(endDate);
                                dto.setEndDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }

                    index = 3;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String departmentName = String.valueOf(currentCell.getNumericCellValue());
                            dto.setDepartmentName(departmentName);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String departmentName = currentCell.getStringCellValue().trim();
                            dto.setDepartmentName(departmentName);
                        }
                    }

                    index = 4;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String professionName = String.valueOf(currentCell.getNumericCellValue());
                            dto.setProfessionName(professionName);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String professionName = currentCell.getStringCellValue().trim();
                            dto.setProfessionName(professionName);
                        }
                    }

                    index = 5;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            Double salaryCofficient = Double.parseDouble(String.valueOf(currentCell.getNumericCellValue()));
                            dto.setSalaryCofficient(salaryCofficient);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            Double salaryCofficient = Double.parseDouble(currentCell.getStringCellValue().trim().replace(",", "."));
                            dto.setSalaryCofficient(salaryCofficient);
                        }
                    }

                    index = 6;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            Double allowanceCoefficient = Double.parseDouble(String.valueOf(currentCell.getNumericCellValue()));
                            dto.setAllowanceCoefficient(allowanceCoefficient);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            Double allowanceCoefficient = Double.parseDouble(currentCell.getStringCellValue().trim().replace(",", "."));
                            dto.setAllowanceCoefficient(allowanceCoefficient);
                        }
                    }

                    index = 7;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String socialInsuranceBookCode = String.valueOf(currentCell.getNumericCellValue());
                            dto.setSocialInsuranceBookCode(socialInsuranceBookCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String socialInsuranceBookCode = currentCell.getStringCellValue().trim();
                            dto.setSocialInsuranceBookCode(socialInsuranceBookCode);
                        }
                    }
                    listData.add(dto);
                }
                rowIndex++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return listData;
    }


    public static List<PositionTitleDto> importPositionTitleFromInputStream(InputStream is) {
        List<PositionTitleDto> listData = new ArrayList<>();
        try {
            // cảnh báo
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            int rowIndex = 1;
            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell = null;
                if (currentRow != null) {
                    PositionTitleDto dto = new PositionTitleDto();

                    Integer index = 0;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String code = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setCode(code);
                            System.out.println(code);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String code = currentCell.getStringCellValue().trim();
                            dto.setCode(code);
                            System.out.println(code);
                        }
                    }
                    index = 1;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String name = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setName(name);
                            System.out.println(name);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String code = currentCell.getStringCellValue().trim();
                            dto.setName(code);
                            System.out.println(code);
                        }
                    }
                    index = 2;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String description = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setDescription(description);
                            System.out.println(description);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String description = currentCell.getStringCellValue().trim();
                            dto.setDescription(description);
                            System.out.println(description);
                        }
                    }
                    index = 3;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String positionCoefficient = String.valueOf(currentCell.getNumericCellValue());
                            dto.setPositionCoefficient(currentCell.getNumericCellValue());
                            System.out.println(positionCoefficient);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String positionCoefficient = currentCell.getStringCellValue().trim();
                            dto.setPositionCoefficient(Double.valueOf(positionCoefficient));
                            System.out.println(positionCoefficient);
                        }
                    }
                    listData.add(dto);
                }
                rowIndex++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return listData;
    }

    public static List<HRDepartmentDto> importDepartmentFromInputStream(InputStream is) {
        List<HRDepartmentDto> listData = new ArrayList<>();
        try {
            // cảnh báo
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            int rowIndex = 1;
            Calendar calendar = Calendar.getInstance();
            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell;
                if (currentRow != null) {
                    HRDepartmentDto dto = new HRDepartmentDto();

                    Integer index = 0;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String code = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setCode(code);
                            System.out.println(code);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String code = currentCell.getStringCellValue().trim();
                            dto.setCode(code);
                            System.out.println(code);
                        }
                    }
                    index = 1;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String parentCode = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setParentCode(parentCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String parentCode = currentCell.getStringCellValue().trim();
                            dto.setParentCode(parentCode);
                        }
                    }
                    index = 2;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String name = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setName(name);
                            System.out.println(name);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String code = currentCell.getStringCellValue().trim();
                            dto.setName(code);
                            System.out.println(code);
                        }
                    }
                    index = 3;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String description = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setDescription(description);
                            System.out.println(description);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String description = currentCell.getStringCellValue().trim();
                            dto.setDescription(description);
                            System.out.println(description);
                        }
                    }
                    index = 4;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String displayOrder = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setDisplayOrder(displayOrder);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String displayOrder = currentCell.getStringCellValue().trim();
                            dto.setDisplayOrder(displayOrder);
                        }
                    }
                    index = 5;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String departmentType = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setDepartmentType((int) currentCell.getNumericCellValue());
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String departmentType = currentCell.getStringCellValue().trim();
                            dto.setDepartmentType(Integer.parseInt(departmentType));
                        }
                    }
                    index = 6;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String foundedDate;
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setFoundedDate(calendar.getTime());
                            }

                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            foundedDate = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(foundedDate);
                                dto.setFoundedDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }
                    index = 7;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String foundedNumber = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setFoundedNumber(foundedNumber);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String foundedNumber = currentCell.getStringCellValue().trim();
                            dto.setFoundedNumber(foundedNumber);
                        }
                    }
                    index = 8;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String shortName = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setShortName(shortName);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String shortName = currentCell.getStringCellValue().trim();
                            dto.setShortName(shortName);
                        }
                    }
                    index = 9;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String departmentDisplayCode = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setDepartmentDisplayCode(departmentDisplayCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String departmentDisplayCode = currentCell.getStringCellValue().trim();
                            dto.setDepartmentDisplayCode(departmentDisplayCode);
                        }
                    }
                    index = 10;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String establishDecisionCode = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setEstablishDecisionCode(establishDecisionCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String establishDecisionCode = currentCell.getStringCellValue().trim();
                            dto.setEstablishDecisionCode(establishDecisionCode);
                        }
                    }
                    index = 11;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String establishDecisionDate = "";

                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setEstablishDecisionDate(calendar.getTime());
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            establishDecisionDate = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(establishDecisionDate);
                                dto.setEstablishDecisionDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }
                    listData.add(dto);
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listData;
    }

    public static List<StaffLabourAgreementDto> importStaffLabourAgreementFromInputStream(InputStream is) {
        List<StaffLabourAgreementDto> listData = new ArrayList<>();
        try {
            // cảnh báo
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            int rowIndex = 1;
            Calendar calendar = Calendar.getInstance();
            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell;
                if (currentRow != null) {
                    StaffLabourAgreementDto dto = new StaffLabourAgreementDto();

                    Integer index = 0;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String staffCode = String.valueOf(currentCell.getNumericCellValue());
                            StaffDto staffDto = new StaffDto();
                            staffDto.setStaffCode(staffCode);
                            dto.setStaffCode(staffCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String staffCode = currentCell.getStringCellValue().trim();
                            StaffDto staffDto = new StaffDto();
                            staffDto.setStaffCode(staffCode);
                            dto.setStaffCode(staffCode);
                        }
                    }

                    index = 1;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String recruitmentDate;

                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setRecruitmentDate(calendar.getTime());
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            recruitmentDate = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(recruitmentDate);
                                dto.setRecruitmentDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }
                    index = 2;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String contractDate;
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setContractDate(calendar.getTime());
                            }

                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            contractDate = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(contractDate);
                                dto.setContractDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }
                    index = 3;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String contractTypeCode = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setContractTypeCode(contractTypeCode);
                            System.out.println(contractTypeCode);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String contractTypeCode = currentCell.getStringCellValue().trim();
                            dto.setContractTypeCode(contractTypeCode);
                            System.out.println(contractTypeCode);
                        }
                    }
                    listData.add(dto);
                }
                rowIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listData;
    }

    public static List<PersonCertificateDto> importPersonCertificateFromInputStream(InputStream is) {
        List<PersonCertificateDto> listData = new ArrayList<>();
        try {
            // cảnh báo
            @SuppressWarnings("resource")
            Workbook workbook = new XSSFWorkbook(is);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            int rowIndex = 1;
            Calendar calendar = Calendar.getInstance();
            int num = datatypeSheet.getLastRowNum();
            while (rowIndex <= num) {
                Row currentRow = datatypeSheet.getRow(rowIndex);
                Cell currentCell;
                if (currentRow != null) {
                    PersonCertificateDto dto = new PersonCertificateDto();

                    Integer index = 0;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String code = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setPersonCode(code);
                            System.out.println(code);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String code = currentCell.getStringCellValue().trim();
                            dto.setPersonCode(code);
                        }
                    }
                    index = 1;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String name = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setName(name);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String name = currentCell.getStringCellValue().trim();
                            dto.setName(name);
                        }
                    }
                    index = 2;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                            String type = String.valueOf((int) currentCell.getNumericCellValue());
                            dto.setCertificateType(type);
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            String type = currentCell.getStringCellValue().trim();
                            dto.setCertificateType(type);
                        }
                    }
                    index = 3;
                    if (index != null) {
                        currentCell = currentRow.getCell(index);
                        String contractDate;
                        if (currentCell != null && currentCell.getCellTypeEnum() == CellType.NUMERIC
                                && currentCell.getNumericCellValue() > 0) {
                            if (DateUtil.isCellDateFormatted(currentCell)) {
                                calendar.setTime(currentCell.getDateCellValue());
                                dto.setIssueDate(calendar.getTime());
                            }
                        } else if (currentCell != null && currentCell.getCellTypeEnum() == CellType.STRING
                                && currentCell.getStringCellValue() != null) {
                            contractDate = String.valueOf(currentCell.getStringCellValue());
                            try {
                                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(contractDate);
                                dto.setIssueDate(date);
                            } catch (Exception ex) {
                                System.out.print(ex.getMessage());
                            }
                        }
                    }
                    listData.add(dto);
                }
                rowIndex++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return listData;
    }
}

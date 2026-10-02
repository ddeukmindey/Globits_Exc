import React from "react";
import { observer } from "mobx-react";
import { useStore } from "app/stores";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Grid,
  MenuItem,
  IconButton,
  Icon,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
  Select,
  InputLabel,
  FormControl
} from "@material-ui/core";
import { Autocomplete } from "@material-ui/lab";
import { Formik, Form, FieldArray } from "formik";
import { MuiPickersUtilsProvider, KeyboardDatePicker, KeyboardTimePicker } from "@material-ui/pickers";
import DateFnsUtils from "@date-io/date-fns";

const TimeSheetDialog = observer(({ handleClose }) => {
  const { timeSheetStore } = useStore();
  const { 
      item, 
      projectList, 
      staffList,
      handleFormSubmit,
  } = timeSheetStore;

  const getStaffDisplayName = (option) => {
    if (!option) return "";
    return (
      option.displayName ||
      `${option.lastName || ""} ${option.firstName || ""}`.trim() ||
      `${option.firstName || ""} ${option.lastName || ""}`.trim() ||
      option.name ||
      option.staffCode ||
      option.code ||
      ""
    );
  };

  const getStaffOptions = (values) => {
      if (values.project && values.project.projectStaff && values.project.projectStaff.length > 0) {
          return values.project.projectStaff;
      }
      return staffList || [];
  };

  return (
    <Dialog open={true} onClose={handleClose} maxWidth="md" fullWidth>
      <DialogTitle>{item.id ? "Sửa TimeSheet" : "Thêm mới TimeSheet"}</DialogTitle>
      
      <Formik
        initialValues={{
            ...item,
            details: item.details || []
        }}
        enableReinitialize={true}
        onSubmit={(values) => {
            handleFormSubmit(values);
        }}
      >
        {({ values, handleChange, setFieldValue, handleSubmit }) => (
          <Form onSubmit={handleSubmit}>
            <DialogContent dividers>
              <Grid container spacing={2}>
                
                <Grid item xs={12} sm={6}>
                  <Autocomplete
                    options={projectList}
                    getOptionLabel={(option) => option.name || option.code}
                    value={values.project || null}
                    onChange={(event, newValue) => {
                        setFieldValue('project', newValue);
                        setFieldValue('timeSheetStaff', []);
                        setFieldValue('details', []);
                    }}
                    getOptionSelected={(option, value) => option.id === value.id}
                    renderInput={(params) => (
                      <TextField
                        {...params}
                        label="Dự án"
                        variant="standard"
                        fullWidth
                        required
                      />
                    )}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <Autocomplete
                    multiple
                    options={getStaffOptions(values)}
                    getOptionLabel={getStaffDisplayName}
                    value={values.timeSheetStaff || []}
                    onChange={(event, newValue) => {
                        setFieldValue('timeSheetStaff', newValue);
                        // Clean up employees in details if they are removed from timeSheetStaff
                        if (values.details && values.details.length > 0) {
                            const staffIds = (newValue || []).map(s => s.id);
                            const updatedDetails = values.details.map(detail => {
                                if (detail.employee && !staffIds.includes(detail.employee.id)) {
                                    return { ...detail, employee: null };
                                }
                                return detail;
                            });
                            setFieldValue('details', updatedDetails);
                        }
                    }}
                    getOptionSelected={(option, value) => option.id === value.id}
                    renderInput={(params) => (
                      <TextField
                        {...params}
                        label="Nhân viên tham gia"
                        variant="standard"
                        fullWidth
                      />
                    )}
                  />
                </Grid>
                
                <MuiPickersUtilsProvider utils={DateFnsUtils}>
                  <Grid item xs={12} sm={4}>
                    <KeyboardDatePicker
                      disableToolbar
                      variant="inline"
                      format="dd/MM/yyyy"
                      margin="normal"
                      label="Ngày làm việc"
                      value={values.workingDate || null}
                      onChange={(date) => setFieldValue('workingDate', date)}
                      KeyboardButtonProps={{
                        'aria-label': 'change date',
                      }}
                      fullWidth
                    />
                  </Grid>
                  <Grid item xs={12} sm={4}>
                    <KeyboardTimePicker
                      margin="normal"
                      label="Giờ bắt đầu"
                      value={values.startTime || null}
                      onChange={(time) => setFieldValue('startTime', time)}
                      KeyboardButtonProps={{
                        'aria-label': 'change time',
                      }}
                      fullWidth
                    />
                  </Grid>
                  <Grid item xs={12} sm={4}>
                    <KeyboardTimePicker
                      margin="normal"
                      label="Giờ kết thúc"
                      value={values.endTime || null}
                      onChange={(time) => setFieldValue('endTime', time)}
                      KeyboardButtonProps={{
                        'aria-label': 'change time',
                      }}
                      fullWidth
                    />
                  </Grid>
                </MuiPickersUtilsProvider>

                <Grid item xs={12} sm={6}>
                  <FormControl fullWidth>
                    <InputLabel>Mức độ ưu tiên</InputLabel>
                    <Select
                      name="priority"
                      value={values.priority || ""}
                      onChange={handleChange}
                    >
                      <MenuItem value={1}>Thấp</MenuItem>
                      <MenuItem value={2}>Trung bình</MenuItem>
                      <MenuItem value={3}>Cao</MenuItem>
                      <MenuItem value={4}>Cấp bách</MenuItem>
                    </Select>
                  </FormControl>
                </Grid>

                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Mô tả"
                    name="description"
                    value={values.description || ""}
                    onChange={handleChange}
                    multiline
                  />
                </Grid>

                <Grid item xs={12}>
                  <h4 style={{ color: '#26c6da', marginTop: '16px', display: 'flex', alignItems: 'center' }}>
                      Chi tiết công việc
                  </h4>
                  <FieldArray name="details">
                      {({ insert, remove, push }) => (
                          <div>
                            <Button 
                              variant="contained" 
                              style={{ backgroundColor: '#26c6da', color: 'white', marginBottom: '16px' }}
                              onClick={() => push({ workingItemTitle: "", employee: null })}
                            >
                              <Icon>add</Icon> Thêm mới
                            </Button>
                            
                            {values.details && values.details.length > 0 && (
                              <Table size="small">
                                <TableHead>
                                  <TableRow>
                                    <TableCell>Tiêu đề công việc</TableCell>
                                    <TableCell>Người thực hiện</TableCell>
                                    <TableCell align="center">Thao tác</TableCell>
                                  </TableRow>
                                </TableHead>
                                <TableBody>
                                  {values.details.map((detail, index) => (
                                    <TableRow key={index}>
                                      <TableCell>
                                        <TextField
                                          fullWidth
                                          variant="outlined"
                                          size="small"
                                          name={`details.${index}.workingItemTitle`}
                                          value={detail.workingItemTitle || ""}
                                          onChange={handleChange}
                                        />
                                      </TableCell>
                                      <TableCell>
                                        <Autocomplete
                                          options={values.timeSheetStaff || []}
                                          getOptionLabel={getStaffDisplayName}
                                          getOptionSelected={(option, value) => option?.id === value?.id}
                                          value={detail.employee || null}
                                          onChange={(event, newValue) => {
                                            setFieldValue(`details.${index}.employee`, newValue);
                                          }}
                                          renderInput={(params) => <TextField {...params} variant="outlined" size="small" />}
                                        />
                                      </TableCell>
                                      <TableCell align="center">
                                        <IconButton onClick={() => remove(index)} color="secondary">
                                          <Icon>delete</Icon>
                                        </IconButton>
                                      </TableCell>
                                    </TableRow>
                                  ))}
                                </TableBody>
                              </Table>
                            )}
                          </div>
                      )}
                  </FieldArray>
                </Grid>

              </Grid>
            </DialogContent>
            <DialogActions>
              <Button onClick={handleClose} color="secondary" variant="contained">
                Hủy
              </Button>
              <Button type="submit" color="primary" variant="contained">
                Lưu
              </Button>
            </DialogActions>
          </Form>
        )}
      </Formik>
    </Dialog>
  );
});

export default TimeSheetDialog;

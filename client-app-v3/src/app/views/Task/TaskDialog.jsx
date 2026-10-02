import React, { useEffect } from "react";
import { observer } from 'mobx-react';
import { useStore } from 'app/stores';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Grid,
  MenuItem,
  FormControl,
  InputLabel,
  Select,
} from "@material-ui/core";
import { Formik, Form } from "formik";
import GlobitsTextField from "app/common/form/GlobitsTextField";

export default observer(function TaskDialog(props) {
  const { open, handleClose } = props;
  const { taskStore } = useStore();
  const { task, handleFormSubmit, itemId, projectList, staffList, loadDropdownData } = taskStore;

  useEffect(() => {
    loadDropdownData();
  }, [loadDropdownData]);

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth={true}>
      <DialogTitle>{itemId ? "Cập nhật Công việc" : "Thêm mới Công việc"}</DialogTitle>
      <Formik
        initialValues={task}
        enableReinitialize={true}
        onSubmit={(values) => {
          handleFormSubmit(values);
        }}
      >
        {({ values, handleChange, handleSubmit }) => (
          <Form onSubmit={handleSubmit}>
            <DialogContent dividers>
              <Grid container spacing={2}>
                <Grid item sm={12} xs={12}>
                  <GlobitsTextField
                    name="name"
                    label="Tên công việc"
                    required
                  />
                </Grid>
                <Grid item sm={6} xs={12}>
                  <FormControl fullWidth variant="outlined" size="small">
                    <InputLabel id="project-label">Dự án</InputLabel>
                    <Select
                      labelId="project-label"
                      label="Dự án"
                      name="projectId"
                      value={values.projectId || ""}
                      onChange={handleChange}
                    >
                      <MenuItem value="">
                        <em>-- Chọn dự án --</em>
                      </MenuItem>
                      {projectList.map((p) => (
                        <MenuItem key={p.id} value={p.id}>
                          {p.name || p.code}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>
                <Grid item sm={6} xs={12}>
                  <FormControl fullWidth variant="outlined" size="small">
                    <InputLabel id="staff-label">Người thực hiện</InputLabel>
                    <Select
                      labelId="staff-label"
                      label="Người thực hiện"
                      name="staffId"
                      value={values.staffId || ""}
                      onChange={handleChange}
                    >
                      <MenuItem value="">
                        <em>-- Chọn nhân viên --</em>
                      </MenuItem>
                      {staffList.map((s) => (
                        <MenuItem key={s.id} value={s.id}>
                          {s.displayName || s.firstName + " " + s.lastName}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>
                <Grid item sm={6} xs={12}>
                  <FormControl fullWidth variant="outlined" size="small">
                    <InputLabel id="priority-label">Mức độ ưu tiên</InputLabel>
                    <Select
                      labelId="priority-label"
                      label="Mức độ ưu tiên"
                      name="priority"
                      value={values.priority || 1}
                      onChange={handleChange}
                    >
                      <MenuItem value={1}>Cao</MenuItem>
                      <MenuItem value={2}>Trung bình</MenuItem>
                      <MenuItem value={3}>Thấp</MenuItem>
                    </Select>
                  </FormControl>
                </Grid>
                <Grid item sm={6} xs={12}>
                  <FormControl fullWidth variant="outlined" size="small">
                    <InputLabel id="status-label">Trạng thái</InputLabel>
                    <Select
                      labelId="status-label"
                      label="Trạng thái"
                      name="status"
                      value={values.status || 1}
                      onChange={handleChange}
                    >
                      <MenuItem value={1}>Mới tạo</MenuItem>
                      <MenuItem value={2}>Đang làm</MenuItem>
                      <MenuItem value={3}>Hoàn thành</MenuItem>
                      <MenuItem value={4}>Tạm hoãn</MenuItem>
                    </Select>
                  </FormControl>
                </Grid>
                <Grid item sm={12} xs={12}>
                  <GlobitsTextField
                    name="description"
                    label="Mô tả công việc"
                    multiline
                    rows={3}
                  />
                </Grid>
              </Grid>
            </DialogContent>
            <DialogActions>
              <Button variant="contained" color="secondary" onClick={handleClose}>
                Hủy
              </Button>
              <Button type="submit" variant="contained" color="primary">
                Lưu
              </Button>
            </DialogActions>
          </Form>
        )}
      </Formik>
    </Dialog>
  );
});

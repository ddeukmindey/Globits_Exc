import React from "react";
import { observer } from 'mobx-react';
import { useStore } from 'app/stores';
import DepartmentParentPopup from './DepartmentParentPopup';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Grid,
} from "@material-ui/core";
import { Formik, Form } from "formik";
import GlobitsTextField from "app/common/form/GlobitsTextField";
import GlobitsNumberInput from "app/common/form/GlobitsNumberInput";
import GlobitsDateTimePicker from "app/common/form/GlobitsDateTimePicker";

export default observer(function DepartmentDialog(props) {
  const { open, handleClose } = props;
  const { departmentStore } = useStore();
  const { 
      department, 
      handleFormSubmit, 
      itemId,
      shouldOpenParentPopup,
      handleOpenParentPopup
  } = departmentStore;

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth={true}>
      <DialogTitle>{itemId ? "Cập nhật Phòng ban" : "Thêm mới Phòng ban"}</DialogTitle>
      
      <Formik
        initialValues={department}
        enableReinitialize={true}
        onSubmit={(values) => {
          handleFormSubmit(values);
        }}
      >
        {({ values, handleSubmit }) => (
          <Form onSubmit={handleSubmit}>
            <DialogContent dividers>
              <Grid container spacing={2}>
                <Grid item sm={6} xs={12}>
                  <GlobitsTextField
                    name="name"
                    label="Tên phòng ban (Name)"
                  />
                </Grid>
                <Grid item sm={6} xs={12}>
                  <GlobitsTextField
                    name="code"
                    label="Mã phòng ban (Code)"
                  />
                </Grid>
                <Grid item sm={12} xs={12}>
                  <GlobitsTextField
                    name="description"
                    label="Mô tả (Description)"
                  />
                </Grid>
                <Grid item sm={6} xs={12}>
                  <GlobitsTextField
                    name="func"
                    label="Chức năng (Func)"
                  />
                </Grid>
                <Grid item sm={6} xs={12}>
                  <GlobitsTextField
                    name="industryBlock"
                    label="Khối ngành (Industry Block)"
                  />
                </Grid>
                <Grid item sm={4} xs={12}>
                  <GlobitsTextField
                    name="foundedNumber"
                    label="Số thành lập (Founded Number)"
                  />
                </Grid>
                <Grid item sm={4} xs={12}>
                  <GlobitsDateTimePicker
                    name="foundedDate"
                    label="Ngày thành lập (Founded Date)"
                  />
                </Grid>
                <Grid item sm={4} xs={12}>
                  <GlobitsNumberInput
                    name="displayOrder"
                    label="Thứ tự hiển thị (Display Order)"
                  />
                </Grid>

                {/* PARENT SELECTION AREA */}
                <Grid item sm={12} xs={12}>
                  <div style={{ display: 'flex', alignItems: 'center', marginTop: '16px' }}>
                      <GlobitsTextField
                        name="parent.name"
                        label="Đơn vị trực thuộc"
                        disabled
                        value={values.parent ? values.parent.name : ""}
                        style={{ marginRight: '16px' }}
                      />
                      <Button 
                        variant="contained" 
                        color="primary" 
                        onClick={handleOpenParentPopup}
                        style={{ whiteSpace: 'nowrap', height: '100%', marginLeft: '16px' }}
                      >
                        Lựa chọn
                      </Button>
                  </div>
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

            {/* POPUP SELECTION */}
            {shouldOpenParentPopup && (
              <DepartmentParentPopup />
            )}
          </Form>
        )}
      </Formik>
    </Dialog>
  );
});

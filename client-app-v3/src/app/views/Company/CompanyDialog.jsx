import React from "react";
import { observer } from 'mobx-react';
import { useStore } from 'app/stores';
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

export default observer(function CompanyDialog(props) {
  const { open, handleClose } = props;
  const { companyStore } = useStore();
  const { company, handleFormSubmit, itemId } = companyStore;

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth={true}>
      <DialogTitle>{itemId ? "Cập nhật Công ty" : "Thêm mới Công ty"}</DialogTitle>
      <Formik
        initialValues={company}
        enableReinitialize={true}
        onSubmit={(values) => {
          handleFormSubmit(values);
        }}
      >
        {({ handleSubmit }) => (
          <Form onSubmit={handleSubmit}>
            <DialogContent dividers>
              <Grid container spacing={2}>
                <Grid item sm={12} xs={12}>
                  <GlobitsTextField
                    name="code"
                    label="Mã công ty (Code)"
                    required
                  />
                </Grid>
                <Grid item sm={12} xs={12}>
                  <GlobitsTextField
                    name="name"
                    label="Tên công ty (Name)"
                    required
                  />
                </Grid>
                <Grid item sm={12} xs={12}>
                  <GlobitsTextField
                    name="address"
                    label="Địa chỉ (Address)"
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

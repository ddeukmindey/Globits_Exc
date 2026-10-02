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

export default observer(function CountryDialog(props) {
  const { open, handleClose } = props;
  const { countryStore } = useStore();
  const { country, handleFormSubmit, itemId } = countryStore;

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth={true}>
      <DialogTitle>{itemId ? "Cập nhật Quốc gia" : "Thêm mới Quốc gia"}</DialogTitle>
      <Formik
        initialValues={country}
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
                    name="name"
                    label="Tên quốc gia (Name)"
                  />
                </Grid>
                <Grid item sm={12} xs={12}>
                  <GlobitsTextField
                    name="code"
                    label="Mã quốc gia (Code)"
                  />
                </Grid>
                <Grid item sm={12} xs={12}>
                  <GlobitsTextField
                    name="description"
                    label="Mô tả (Description)"
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
  )
})

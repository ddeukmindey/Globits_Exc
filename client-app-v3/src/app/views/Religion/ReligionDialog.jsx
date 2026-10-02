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

export default observer(function ReligionDialog(props) {
  const { open, handleClose } = props;
  const { religionStore } = useStore();
  const { religion, handleFormSubmit, itemId } = religionStore;

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth={true}>
      <DialogTitle>{itemId ? "Cập nhật Tôn giáo" : "Thêm mới Tôn giáo"}</DialogTitle>
      <Formik
        initialValues={religion}
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
                    label="Tên tôn giáo (Name)"
                  />
                </Grid>
                <Grid item sm={12} xs={12}>
                  <GlobitsTextField
                    name="code"
                    label="Mã tôn giáo (Code)"
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

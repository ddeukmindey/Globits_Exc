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

export default observer(function FamilyRelationshipDialog(props) {
  const { open, handleClose } = props;
  const { familyRelationshipStore } = useStore();
  const { familyRelationship, handleFormSubmit, itemId } = familyRelationshipStore;

  return (
    <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth={true}>
      <DialogTitle>{itemId ? "Cập nhật Quan hệ gia đình" : "Thêm mới Quan hệ gia đình"}</DialogTitle>
      <Formik
        initialValues={familyRelationship}
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
                    label="Tên quan hệ (Name)"
                  />
                </Grid>
                <Grid item sm={12} xs={12}>
                  <GlobitsTextField
                    name="code"
                    label="Mã quan hệ (Code)"
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

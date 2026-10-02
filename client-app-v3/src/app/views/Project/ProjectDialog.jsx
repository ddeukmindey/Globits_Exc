import React from "react";
import { observer } from "mobx-react";
import { useStore } from "../../stores";
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
import { Autocomplete } from "@material-ui/lab";
import { TextField } from "@material-ui/core";

const ProjectDialog = observer(({ handleClose }) => {
  const { projectStore } = useStore();
  const { item, staffList, handleFormSubmit } = projectStore;

  return (
    <Dialog open={true} onClose={handleClose} maxWidth="sm" fullWidth>
      <DialogTitle>{item.id ? "Sửa dự án" : "Thêm mới dự án"}</DialogTitle>
      
      <Formik
        initialValues={{
          ...item,
          projectStaff: item.projectStaff || []
        }}
        enableReinitialize={true}
        onSubmit={(values) => {
          handleFormSubmit(values);
        }}
      >
        {({ values, setFieldValue, handleSubmit }) => (
          <Form onSubmit={handleSubmit}>
            <DialogContent dividers>
              <Grid container spacing={2}>
                <Grid item xs={12} sm={6}>
                  <GlobitsTextField
                    name="code"
                    label="Mã dự án"
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <GlobitsTextField
                    name="name"
                    label="Tên dự án"
                  />
                </Grid>
                <Grid item xs={12}>
                  <GlobitsTextField
                    name="description"
                    label="Mô tả"
                    multiline
                  />
                </Grid>
                <Grid item xs={12}>
                  <Autocomplete
                    multiple
                    options={staffList}
                    getOptionLabel={(option) => 
                      option.displayName || `${option.firstName || ""} ${option.lastName || ""}`.trim() || option.code || option.id
                    }
                    value={values.projectStaff || []}
                    onChange={(event, newValue) => {
                      setFieldValue('projectStaff', newValue);
                    }}
                    getOptionSelected={(option, value) => option.id === value.id}
                    renderInput={(params) => (
                      <TextField
                        {...params}
                        label="Nhân viên tham gia"
                        variant="outlined"
                        size="small"
                        fullWidth
                      />
                    )}
                  />
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

export default ProjectDialog;

import React from "react";
import { TextField } from "@material-ui/core";
import { useField } from "formik";
import moment from "moment";

const GlobitsDateTimePicker = ({
  name,
  size,
  format,
  variant,
  ...otherProps
}) => {
  const [field, meta] = useField(name);

  let formattedValue = "";
  if (field.value) {
    const d = moment(field.value);
    if (d.isValid()) {
      formattedValue = d.format("YYYY-MM-DD");
    }
  }

  const configDateTimePicker = {
    ...field,
    value: formattedValue,
    ...otherProps,
    type: "date",
    variant: variant ? variant : "outlined",
    size: size ? size : "small",
    fullWidth: true,
    InputLabelProps: {
      shrink: true,
    },
  };

  if (meta && meta.touched && meta.error) {
    configDateTimePicker.error = true;
    configDateTimePicker.helperText = meta.error;
  }

  return <TextField {...configDateTimePicker} />;
};

export default GlobitsDateTimePicker;

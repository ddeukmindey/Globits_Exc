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
    TextField,
    Select,
    MenuItem,
    InputLabel,
    FormControl,
    IconButton,
    Icon,
    Avatar,
} from "@material-ui/core";
import Autocomplete from "@material-ui/lab/Autocomplete";
import { Formik, Form, FieldArray } from "formik";
import { uploadStaffAvatar, getAvatarUrl } from "./StaffService";

export default observer(function StaffDialog(props) {
    const { open, handleClose } = props;
    const { staffStore } = useStore();
    const {
        item,
        handleFormSubmit,
        itemId,
        countryList,
        ethnicsList,
        religionList,
        departmentList,
        relationshipList,
    } = staffStore;

    return (
        <Dialog open={open} onClose={handleClose} maxWidth="lg" fullWidth={true}>
            <DialogTitle>{itemId ? "Cập nhật Thông tin Nhân viên" : "Thêm mới Nhân viên"}</DialogTitle>
            
            <Formik
                initialValues={{
                    ...item,
                    familyRelationships: item.familyRelationships || []
                }}
                enableReinitialize={true}
                onSubmit={(values) => {
                    handleFormSubmit(values);
                }}
            >
                {({ values, handleChange, setFieldValue, handleSubmit }) => (
                    <Form onSubmit={handleSubmit}>
                        <DialogContent dividers style={{ padding: '24px' }}>
                            {/* =======================================================
                            SECTION 1: PERSONAL INFORMATION
                            ======================================================= */}
                            <h4 style={{ color: '#26c6da', marginTop: 0 }}>I. Thông tin chung</h4>
                            <Grid container spacing={2} alignItems="center" style={{ marginBottom: 16 }}>
                                <Grid item xs={12} sm={3} style={{ textAlign: "center" }}>
                                    <Avatar
                                        src={getAvatarUrl(values.avatar || values.imagePath)}
                                        style={{ width: 100, height: 100, margin: "0 auto 8px auto", border: "2px solid #26c6da" }}
                                    >
                                        <Icon style={{ fontSize: 60 }}>person</Icon>
                                    </Avatar>
                                    <div>
                                        <input
                                            accept="image/*"
                                            style={{ display: "none" }}
                                            id="staff-avatar-upload"
                                            type="file"
                                            onChange={async (e) => {
                                                const file = e.target.files[0];
                                                if (file) {
                                                    try {
                                                        const res = await uploadStaffAvatar(file, itemId);
                                                        if (res && res.data && res.data.avatar) {
                                                            setFieldValue("avatar", res.data.avatar);
                                                            setFieldValue("imagePath", res.data.avatar);
                                                        }
                                                    } catch (err) {
                                                        console.error("Upload avatar failed", err);
                                                    }
                                                }
                                            }}
                                        />
                                        <label htmlFor="staff-avatar-upload">
                                            <Button
                                                variant="outlined"
                                                color="primary"
                                                size="small"
                                                component="span"
                                                startIcon={<Icon>cloud_upload</Icon>}
                                            >
                                                Tải ảnh lên
                                            </Button>
                                        </label>
                                        {(values.avatar || values.imagePath) && (
                                            <Button
                                                size="small"
                                                color="secondary"
                                                onClick={() => {
                                                    setFieldValue("avatar", "");
                                                    setFieldValue("imagePath", "");
                                                }}
                                                style={{ marginLeft: 4 }}
                                            >
                                                Xóa
                                            </Button>
                                        )}
                                    </div>
                                </Grid>
                                <Grid item xs={12} sm={9}>
                                    <Grid container spacing={2}>
                                        <Grid item md={4} sm={6} xs={12}>
                                            <TextField
                                                fullWidth variant="outlined" size="small"
                                                name="lastName" label="Họ (Last Name)"
                                                value={values.lastName || ""} 
                                                onChange={(e) => {
                                                    handleChange(e);
                                                    setFieldValue('displayName', `${e.target.value} ${values.firstName || ""}`.trim());
                                                }}
                                            />
                                        </Grid>
                                        <Grid item md={4} sm={6} xs={12}>
                                            <TextField
                                                fullWidth variant="outlined" size="small"
                                                name="firstName" label="Tên (First Name)"
                                                value={values.firstName || ""} 
                                                onChange={(e) => {
                                                    handleChange(e);
                                                    setFieldValue('displayName', `${values.lastName || ""} ${e.target.value}`.trim());
                                                }}
                                            />
                                        </Grid>
                                        <Grid item md={4} sm={6} xs={12}>
                                            <TextField
                                                fullWidth variant="outlined" size="small" disabled
                                                name="displayName" label="Tên hiển thị"
                                                value={values.displayName || ""} 
                                            />
                                        </Grid>
                                    </Grid>
                                </Grid>
                            </Grid>
                            <Grid container spacing={2}>

                                <Grid item md={3} sm={6} xs={12}>
                                    <FormControl fullWidth variant="outlined" size="small">
                                        <InputLabel>Giới tính</InputLabel>
                                        <Select
                                            name="gender"
                                            value={values.gender || "U"}
                                            onChange={handleChange}
                                            label="Giới tính"
                                        >
                                            <MenuItem value="M">Nam</MenuItem>
                                            <MenuItem value="F">Nữ</MenuItem>
                                            <MenuItem value="U">Chưa rõ</MenuItem>
                                        </Select>
                                    </FormControl>
                                </Grid>
                                <Grid item md={3} sm={6} xs={12}>
                                    <TextField
                                        fullWidth variant="outlined" size="small"
                                        type="date"
                                        InputLabelProps={{ shrink: true }}
                                        name="birthDate" label="Ngày sinh"
                                        value={values.birthDate || ""} onChange={handleChange}
                                    />
                                </Grid>
                                <Grid item md={3} sm={6} xs={12}>
                                    <TextField
                                        fullWidth variant="outlined" size="small"
                                        name="idNumber" label="Số CCCD"
                                        value={values.idNumber || ""} onChange={handleChange}
                                    />
                                </Grid>
                                <Grid item md={3} sm={6} xs={12}>
                                    <TextField
                                        fullWidth variant="outlined" size="small"
                                        name="phoneNumber" label="Số điện thoại"
                                        value={values.phoneNumber || ""} onChange={handleChange}
                                    />
                                </Grid>

                                <Grid item md={4} sm={6} xs={12}>
                                    <TextField
                                        fullWidth variant="outlined" size="small"
                                        name="birthPlace" label="Nơi sinh"
                                        value={values.birthPlace || ""} onChange={handleChange}
                                    />
                                </Grid>
                                <Grid item md={4} sm={6} xs={12}>
                                    <TextField
                                        fullWidth variant="outlined" size="small"
                                        name="permanentResidence" label="Hộ khẩu thường trú"
                                        value={values.permanentResidence || ""} onChange={handleChange}
                                    />
                                </Grid>
                                <Grid item md={4} sm={6} xs={12}>
                                    <TextField
                                        fullWidth variant="outlined" size="small"
                                        name="currentResidence" label="Nơi ở hiện nay"
                                        value={values.currentResidence || ""} onChange={handleChange}
                                    />
                                </Grid>
                                <Grid item md={12} sm={12} xs={12}>
                                    <TextField
                                        fullWidth variant="outlined" size="small"
                                        name="email" label="Email"
                                        value={values.email || ""} onChange={handleChange}
                                    />
                                </Grid>
                            </Grid>

                            {/* =======================================================
                            SECTION 2: LOOKUP DICTIONARY
                            ======================================================= */}
                            <h4 style={{ color: '#26c6da', marginTop: '24px' }}>II. Tổ chức & Danh mục</h4>
                            <Grid container spacing={2}>
                                <Grid item md={3} sm={6} xs={12}>
                                    <Autocomplete
                                        options={countryList}
                                        getOptionLabel={(option) => option.name || ""}
                                        value={values.nationality || null}
                                        onChange={(event, newValue) => {
                                            setFieldValue('nationality', newValue);
                                        }}
                                        renderInput={(params) => <TextField {...params} label="Quốc tịch" variant="outlined" size="small" />}
                                    />
                                </Grid>
                                <Grid item md={3} sm={6} xs={12}>
                                    <Autocomplete
                                        options={ethnicsList}
                                        getOptionLabel={(option) => option.name || ""}
                                        value={values.ethnics || null}
                                        onChange={(event, newValue) => {
                                            setFieldValue('ethnics', newValue);
                                        }}
                                        renderInput={(params) => <TextField {...params} label="Dân tộc" variant="outlined" size="small" />}
                                    />
                                </Grid>
                                <Grid item md={3} sm={6} xs={12}>
                                    <Autocomplete
                                        options={religionList}
                                        getOptionLabel={(option) => option.name || ""}
                                        value={values.religion || null}
                                        onChange={(event, newValue) => {
                                            setFieldValue('religion', newValue);
                                        }}
                                        renderInput={(params) => <TextField {...params} label="Tôn giáo" variant="outlined" size="small" />}
                                    />
                                </Grid>
                                <Grid item md={3} sm={6} xs={12}>
                                    <Autocomplete
                                        options={departmentList}
                                        getOptionLabel={(option) => option.name || ""}
                                        value={values.department || null}
                                        onChange={(event, newValue) => {
                                            setFieldValue('department', newValue);
                                        }}
                                        renderInput={(params) => <TextField {...params} label="Phòng ban trực thuộc" variant="outlined" size="small" />}
                                    />
                                </Grid>
                            </Grid>

                            {/* =======================================================
                            SECTION 3: FAMILY RELATIONSHIPS (ARRAY)
                            ======================================================= */}
                            <h4 style={{ color: '#26c6da', marginTop: '24px', display: 'flex', alignItems: 'center' }}>
                                III. Quản lý thân nhân
                            </h4>

                            <FieldArray name="familyRelationships">
                                {({ insert, remove, push }) => (
                                    <div>
                                        <Button
                                            variant="contained"
                                            style={{ backgroundColor: '#26c6da', color: "white", marginBottom: '16px' }}
                                            onClick={() => push({
                                                fullName: "",
                                                profession: "",
                                                birthDate: "",
                                                familyRelationship: null,
                                                address: "",
                                                description: ""
                                            })}
                                        >
                                            <Icon style={{ marginRight: '8px' }}>add</Icon> Thêm mới thân nhân
                                        </Button>
                                        
                                        {values.familyRelationships && values.familyRelationships.length > 0 && (
                                            <div style={{ overflowX: 'auto' }}>
                                                <table style={{ width: '100%', borderCollapse: 'collapse', minWidth: '800px' }}>
                                                    <thead>
                                                        <tr style={{ borderBottom: '1px solid #ccc' }}>
                                                            <th style={{ textAlign: 'left', padding: '8px' }}>Tên</th>
                                                            <th style={{ textAlign: 'left', padding: '8px' }}>Nghề nghiệp</th>
                                                            <th style={{ textAlign: 'left', padding: '8px', width: '160px' }}>Ngày sinh</th>
                                                            <th style={{ textAlign: 'left', padding: '8px', width: '200px' }}>Quan hệ</th>
                                                            <th style={{ textAlign: 'left', padding: '8px' }}>Địa chỉ</th>
                                                            <th style={{ textAlign: 'left', padding: '8px' }}>Mô tả</th>
                                                            <th style={{ textAlign: 'center', padding: '8px', width: '60px' }}>Thao tác</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        {values.familyRelationships.map((rowItem, index) => (
                                                            <tr key={index} style={{ borderBottom: '1px solid #eee' }}>
                                                                <td style={{ padding: '8px' }}>
                                                                    <TextField
                                                                        fullWidth variant="outlined" size="small"
                                                                        name={`familyRelationships.${index}.fullName`}
                                                                        value={rowItem.fullName || ''}
                                                                        onChange={handleChange}
                                                                    />
                                                                </td>
                                                                <td style={{ padding: '8px' }}>
                                                                    <TextField
                                                                        fullWidth variant="outlined" size="small"
                                                                        name={`familyRelationships.${index}.profession`}
                                                                        value={rowItem.profession || ''}
                                                                        onChange={handleChange}
                                                                    />
                                                                </td>
                                                                <td style={{ padding: '8px' }}>
                                                                    <TextField
                                                                        fullWidth variant="outlined" size="small" type="date"
                                                                        InputLabelProps={{ shrink: true }}
                                                                        name={`familyRelationships.${index}.birthDate`}
                                                                        value={rowItem.birthDate || ''}
                                                                        onChange={handleChange}
                                                                    />
                                                                </td>
                                                                <td style={{ padding: '8px' }}>
                                                                    <Autocomplete
                                                                        options={relationshipList}
                                                                        getOptionLabel={(option) => option.name || ""}
                                                                        value={rowItem.familyRelationship || null}
                                                                        onChange={(event, newValue) => {
                                                                            setFieldValue(`familyRelationships.${index}.familyRelationship`, newValue);
                                                                        }}
                                                                        renderInput={(params) => <TextField {...params} variant="outlined" size="small" />}
                                                                    />
                                                                </td>
                                                                <td style={{ padding: '8px' }}>
                                                                    <TextField
                                                                        fullWidth variant="outlined" size="small"
                                                                        name={`familyRelationships.${index}.address`}
                                                                        value={rowItem.address || ''}
                                                                        onChange={handleChange}
                                                                    />
                                                                </td>
                                                                <td style={{ padding: '8px' }}>
                                                                    <TextField
                                                                        fullWidth variant="outlined" size="small"
                                                                        name={`familyRelationships.${index}.description`}
                                                                        value={rowItem.description || ''}
                                                                        onChange={handleChange}
                                                                    />
                                                                </td>
                                                                <td style={{ padding: '8px', textAlign: 'center' }}>
                                                                    <IconButton onClick={() => remove(index)} size="small">
                                                                        <Icon color="error">delete</Icon>
                                                                    </IconButton>
                                                                </td>
                                                            </tr>
                                                        ))}
                                                    </tbody>
                                                </table>
                                            </div>
                                        )}
                                    </div>
                                )}
                            </FieldArray>

                        </DialogContent>
                        <DialogActions style={{ padding: '16px' }}>
                            <Button variant="contained" color="secondary" onClick={handleClose}>Hủy</Button>
                            <Button type="submit" variant="contained" color="primary">Lưu</Button>
                        </DialogActions>
                    </Form>
                )}
            </Formik>
        </Dialog>
    );
});
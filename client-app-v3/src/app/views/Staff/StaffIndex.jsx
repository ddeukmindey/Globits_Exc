import React, { useEffect, useState } from 'react';
import { observer } from 'mobx-react';
import { useStore } from 'app/stores';
import GlobitsTable from 'app/common/GlobitsTable';
import GlobitsSearchInput from 'app/common/GlobitsSearchInput';
import StaffDialog from './StaffDialog';
import GlobitsConfirmationDialog from 'app/common/GlobitsConfirmationDialog';
import { Button, Grid, Icon, IconButton, TextField, Collapse, Avatar } from "@material-ui/core";
import Autocomplete from "@material-ui/lab/Autocomplete";
import { getAvatarUrl } from "./StaffService";

export default observer(function StaffIndex() {
    const [showFilter, setShowFilter] = useState(false);
    const { staffStore } = useStore();
    const {
        itemList,
        totalElements,
        totalPages,
        page,
        pageSize,
        shouldOpenDialog,
        shouldOpenConfirmationDialog,
        setPage,
        setPageSize,
        setKeyword,
        setFilter,
        fetchLookupLists,
        departmentList,
        ethnicsList,
        religionList,
        countryList,
        projectList,
        selectedDepartmentId,
        selectedEthnicsId,
        selectedReligionId,
        selectedCountryId,
        selectedProjectId,
        handleEditItem,
        handleAddItem,
        handleCloseDialog,
        handleDeleteItem,
        handleConfirmDelete,
        handleCloseConfirmationDialog,
        updatePageData,
    } = staffStore;

    useEffect(() => {
        updatePageData();
        fetchLookupLists();
    }, [updatePageData]);

    const handleChangePage = (event, newPage) => {
        setPage(newPage)
    }

    const setRowsPerPage = (event) => {
        setPageSize(Number(event.target.value))
    }

    const handleSearch = (searchObject) => {
        setKeyword(searchObject.keyword);
    };

    const columns = [
        {
            title: "Ảnh",
            field: "avatar",
            width: "80",
            align: "center",
            render: (rowData) => (
                <Avatar
                    src={getAvatarUrl(rowData.avatar || rowData.imagePath)}
                    alt={rowData.displayName}
                    style={{ width: 36, height: 36, margin: "auto" }}
                >
                    <Icon>person</Icon>
                </Avatar>
            )
        },
        { title: "Mã NV (CCCD)", field: "idNumber", width: "150" },
        { title: "Họ và tên", field: "displayName", width: "200" },
        {
            title: "Giới tính",
            field: "gender",
            width: "120",
            render: (rowData) => {
                if (rowData.gender === 'M') return "Nam";
                if (rowData.gender === 'F') return "Nữ";
                return "Chưa rõ";
            }
        },
        { title: "SĐT", field: "phoneNumber", width: "150" },
        { title: "Email", field: "email", width: "200" },
        { title: "Phòng ban", field: "department.name", width: "150" },
        {
            title: "Hành động", field: "custom", align: "center", width: "150",
            render: (rowData) => (
                <div className='flex flex-middle flex-center'>
                    <IconButton onClick={() => handleEditItem(rowData)}>
                        <Icon color="primary">edit</Icon>
                    </IconButton>
                    <IconButton onClick={() => handleDeleteItem(rowData.id)}>
                        <Icon color="error">delete</Icon>
                    </IconButton>
                </div>
            )
        }
    ]

    return (
        <div className='m-sm-30'>
            <div className="mb-sm-30">
                <h2 style={{ margin: 0, marginBottom: '16px' }}>Quản lý nhân viên</h2>
                <Grid container spacing={2} justifyContent='space-between' alignItems='center'>
                    <Grid item lg={6} md={6} sm={6} xs={12}>
                        <Button variant='contained' color='primary' onClick={handleAddItem}>Thêm mới</Button>
                    </Grid>
                    <Grid item lg={6} md={6} sm={6} xs={12}>
                        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end' }}>
                            <div style={{ width: '100%', maxWidth: '350px', marginBottom: '8px' }}>
                                <GlobitsSearchInput search={handleSearch} />
                            </div>
                            <Button variant='contained' color='secondary' onClick={() => setShowFilter(!showFilter)}>
                                {showFilter ? "Ẩn Bộ Lọc" : "Bộ Lọc"}
                            </Button>
                        </div>
                    </Grid>
                </Grid>
                <Collapse in={showFilter}>
                    <Grid container spacing={2} style={{ marginTop: '16px', marginBottom: '16px' }}>
                        <Grid item lg={2} md={4} sm={6} xs={12}>
                            <Autocomplete
                                options={departmentList || []}
                                getOptionLabel={(option) => option.name || ""}
                                value={(departmentList || []).find((opt) => opt.id === selectedDepartmentId) || null}
                                onChange={(event, newValue) => setFilter('selectedDepartmentId', newValue ? newValue.id : null)}
                                renderInput={(params) => <TextField {...params} label="Phòng ban" variant="outlined" size="small" />}
                            />
                        </Grid>
                        <Grid item lg={2} md={4} sm={6} xs={12}>
                            <Autocomplete
                                options={ethnicsList || []}
                                getOptionLabel={(option) => option.name || ""}
                                value={(ethnicsList || []).find((opt) => opt.id === selectedEthnicsId) || null}
                                onChange={(event, newValue) => setFilter('selectedEthnicsId', newValue ? newValue.id : null)}
                                renderInput={(params) => <TextField {...params} label="Dân tộc" variant="outlined" size="small" />}
                            />
                        </Grid>
                        <Grid item lg={2} md={4} sm={6} xs={12}>
                            <Autocomplete
                                options={religionList || []}
                                getOptionLabel={(option) => option.name || ""}
                                value={(religionList || []).find((opt) => opt.id === selectedReligionId) || null}
                                onChange={(event, newValue) => setFilter('selectedReligionId', newValue ? newValue.id : null)}
                                renderInput={(params) => <TextField {...params} label="Tôn giáo" variant="outlined" size="small" />}
                            />
                        </Grid>
                        <Grid item lg={3} md={6} sm={6} xs={12}>
                            <Autocomplete
                                options={countryList || []}
                                getOptionLabel={(option) => option.name || ""}
                                value={(countryList || []).find((opt) => opt.id === selectedCountryId) || null}
                                onChange={(event, newValue) => setFilter('selectedCountryId', newValue ? newValue.id : null)}
                                renderInput={(params) => <TextField {...params} label="Quốc gia" variant="outlined" size="small" />}
                            />
                        </Grid>
                        <Grid item lg={3} md={6} sm={6} xs={12}>
                            <Autocomplete
                                options={projectList || []}
                                getOptionLabel={(option) => option.name || ""}
                                value={(projectList || []).find((opt) => opt.id === selectedProjectId) || null}
                                onChange={(event, newValue) => setFilter('selectedProjectId', newValue ? newValue.id : null)}
                                renderInput={(params) => <TextField {...params} label="Dự án" variant="outlined" size="small" />}
                            />
                        </Grid>
                    </Grid>
                </Collapse>
            </div>
            <GlobitsTable
                data={itemList.slice()}
                columns={columns}
                totalPages={totalPages}
                handleChangePage={handleChangePage}
                setRowsPerPage={setRowsPerPage}
                pageSize={pageSize}
                pageSizeOption={[5, 10, 25]}
                totalElements={totalElements}
                page={page}
                selection={false}
                handleSelectList={() => { }}
            />
            {shouldOpenDialog && (
                <StaffDialog
                    open={shouldOpenDialog}
                    handleClose={handleCloseDialog}
                />
            )}
            {shouldOpenConfirmationDialog && (
                <GlobitsConfirmationDialog
                    title="Xác nhận xóa"
                    text="Bạn có chắc chắn muốn xóa nhân viên này không?"
                    open={shouldOpenConfirmationDialog}
                    onConfirmDialogClose={handleCloseConfirmationDialog}
                    onYesClick={handleConfirmDelete}
                    agree="Xác nhận"
                    cancel="Hủy"
                />
            )}
        </div >
    )
})
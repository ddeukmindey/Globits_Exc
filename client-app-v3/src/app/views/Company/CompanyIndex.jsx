import React, { useEffect } from 'react';
import { observer } from 'mobx-react';
import { useStore } from 'app/stores';
import MaterialTable from 'material-table';
import { Button, Icon, IconButton, TextField, TablePagination, Grid } from '@material-ui/core';
import CompanyDialog from './CompanyDialog';
import GlobitsConfirmationDialog from 'app/common/GlobitsConfirmationDialog';

export default observer(function CompanyIndex() {
    const { companyStore } = useStore();
    const {
        companyList,
        totalElements,
        page,
        pageSize,
        setPage,
        setPageSize,
        setKeyword,
        updatePageData,
        handleAddItem,
        handleEditItem,
        handleDeleteItem,
        shouldOpenDialog,
        handleCloseDialog,
        shouldOpenConfirmationDialog,
        handleCloseConfirmationDialog,
        handleConfirmDelete
    } = companyStore;

    useEffect(() => {
        updatePageData();
    }, [updatePageData]);

    const columns = [
        { title: 'Mã công ty', field: 'code' },
        { title: 'Tên công ty', field: 'name' },
        { title: 'Địa chỉ', field: 'address' },
        {
            title: 'Hành động',
            render: (rowData) => (
                <div>
                    <IconButton color="primary" onClick={() => handleEditItem(rowData)}>
                        <Icon>edit</Icon>
                    </IconButton>
                    <IconButton color="secondary" onClick={() => handleDeleteItem(rowData.id)}>
                        <Icon>delete</Icon>
                    </IconButton>
                </div>
            )
        }
    ];

    const handleChangePage = (event, newPage) => {
        setPage(newPage + 1);
    };

    const handleChangeRowsPerPage = (event) => {
        setPageSize(parseInt(event.target.value, 10));
    };

    return (
        <div className="m-sm-30">
            <div className="mb-sm-30">
                <h2>Quản lý Công ty</h2>
            </div>
            <Grid container spacing={2} justify="space-between" alignItems="center">
                <Grid item xs={12} sm={4}>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleAddItem}
                        startIcon={<Icon>add</Icon>}
                    >
                        Thêm mới
                    </Button>
                </Grid>
                <Grid item xs={12} sm={4}>
                    <TextField
                        variant="outlined"
                        size="small"
                        fullWidth
                        placeholder="Tìm kiếm công ty..."
                        onChange={(e) => setKeyword(e.target.value)}
                    />
                </Grid>
            </Grid>
            <div className="mt-16">
                <MaterialTable
                    title=""
                    data={companyList}
                    columns={columns}
                    options={{
                        selection: false,
                        actionsColumnIndex: -1,
                        paging: false,
                        search: false,
                        toolbar: false
                    }}
                    localization={{
                        body: {
                            emptyDataSourceMessage: "Không có dữ liệu"
                        }
                    }}
                />
                <TablePagination
                    rowsPerPageOptions={[5, 10, 25]}
                    component="div"
                    count={totalElements}
                    rowsPerPage={pageSize}
                    page={page - 1}
                    onChangePage={handleChangePage}
                    onChangeRowsPerPage={handleChangeRowsPerPage}
                    labelRowsPerPage="Số hàng mỗi trang:"
                />
            </div>
            {shouldOpenDialog && (
                <CompanyDialog
                    open={shouldOpenDialog}
                    handleClose={handleCloseDialog}
                />
            )}
            <GlobitsConfirmationDialog
                open={shouldOpenConfirmationDialog}
                onConfirmDialogClose={handleCloseConfirmationDialog}
                onYesClick={handleConfirmDelete}
                title="Xác nhận xóa"
                text="Bạn có chắc chắn muốn xóa công ty này không?"
                agree="Xóa"
                cancel="Hủy"
            />
        </div>
    );
});

import React, { useEffect } from 'react';
import { observer } from 'mobx-react';
import { useStore } from 'app/stores';
import MaterialTable from 'material-table';
import { Button, Icon, IconButton, TextField, TablePagination, Grid } from '@material-ui/core';
import CountryDialog from './CountryDialog';
import GlobitsConfirmationDialog from 'app/common/GlobitsConfirmationDialog';

export default observer(function CountryIndex() {
    const { countryStore } = useStore();
    const {
        countryList,
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
    } = countryStore;

    useEffect(() => {
        updatePageData();
    }, [updatePageData]);

    const columns = [
        { title: 'Mã quốc gia', field: 'code' },
        { title: 'Tên quốc gia', field: 'name' },
        { title: 'Mô tả', field: 'description' },
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

    // Thay đổi trang
    const handleChangePage = (event, newPage) => {
        setPage(newPage + 1); // Material UI TablePagination bắt đầu từ 0
    };

    // Thay đổi số lượng bản ghi
    const handleChangeRowsPerPage = (event) => {
        setPageSize(parseInt(event.target.value, 10));
    };

    // Tìm kiếm (có thể dùng timeout debounce nhưng đây làm đơn giản trước)
    const handleSearchChange = (event) => {
        setKeyword(event.target.value);
    };

    return (
        <div style={{ padding: '20px' }}>
            <Grid container spacing={2} justifyContent="space-between" alignItems="center" style={{ marginBottom: '16px' }}>
                <Grid item>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleAddItem}
                    >
                        Thêm mới Quốc gia
                    </Button>
                </Grid>
                <Grid item>
                    <TextField
                        label="Tìm kiếm quốc gia..."
                        variant="outlined"
                        size="small"
                        onChange={handleSearchChange}
                        style={{ width: '300px' }}
                    />
                </Grid>
            </Grid>

            <MaterialTable
                title="Danh sách Quốc gia"
                columns={columns}
                data={countryList.slice()}
                options={{
                    search: false,   // Tắt search client-side mặc định
                    paging: false,   // Tắt paging client-side mặc định
                    headerStyle: {
                        backgroundColor: '#01579b',
                        color: '#FFF'
                    }
                }}
            />

            <TablePagination
                component="div"
                count={totalElements}
                page={page - 1} // Vì page của API bắt đầu từ 1, UI từ 0
                onPageChange={handleChangePage}
                rowsPerPage={pageSize}
                onRowsPerPageChange={handleChangeRowsPerPage}
                rowsPerPageOptions={[5, 10, 25, 50]}
                labelRowsPerPage="Số bản ghi mỗi trang:"
            />

            {shouldOpenDialog && (
                <CountryDialog
                    open={shouldOpenDialog}
                    handleClose={handleCloseDialog}
                />
            )}

            {shouldOpenConfirmationDialog && (
                <GlobitsConfirmationDialog
                    open={shouldOpenConfirmationDialog}
                    onConfirmDialogClose={handleCloseConfirmationDialog}
                    onYesClick={handleConfirmDelete}
                    title="Xác nhận xóa"
                    text="Bạn có chắc chắn muốn xóa bản ghi này không?"
                    agree="Xóa"
                    cancel="Hủy"
                />
            )}
        </div>
    );
});
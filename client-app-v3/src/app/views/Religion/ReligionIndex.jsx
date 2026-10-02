import React, { useEffect } from 'react';
import { observer } from 'mobx-react';
import { useStore } from 'app/stores';
import MaterialTable from 'material-table';
import { Button, Icon, IconButton, TextField, TablePagination, Grid } from '@material-ui/core';
import ReligionDialog from './ReligionDialog';
import GlobitsConfirmationDialog from 'app/common/GlobitsConfirmationDialog';

export default observer(function ReligionIndex() {
    const { religionStore } = useStore();
    const {
        religionList,
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
    } = religionStore;

    useEffect(() => {
        updatePageData();
    }, [updatePageData]);

    const columns = [
        { title: 'Mã tôn giáo', field: 'code' },
        { title: 'Tên tôn giáo', field: 'name' },
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

    const handleChangePage = (event, newPage) => {
        setPage(newPage + 1);
    };

    const handleChangeRowsPerPage = (event) => {
        setPageSize(parseInt(event.target.value, 10));
    };

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
                        Thêm mới Tôn giáo
                    </Button>
                </Grid>
                <Grid item>
                    <TextField
                        label="Tìm kiếm tôn giáo..."
                        variant="outlined"
                        size="small"
                        onChange={handleSearchChange}
                        style={{ width: '300px' }}
                    />
                </Grid>
            </Grid>

            <MaterialTable
                title="Danh sách Tôn giáo"
                columns={columns}
                data={religionList.slice()}
                options={{
                    search: false,
                    paging: false,
                    headerStyle: {
                        backgroundColor: '#01579b',
                        color: '#FFF'
                    }
                }}
            />

            <TablePagination
                component="div"
                count={totalElements}
                page={page - 1}
                onPageChange={handleChangePage}
                rowsPerPage={pageSize}
                onRowsPerPageChange={handleChangeRowsPerPage}
                rowsPerPageOptions={[5, 10, 25, 50]}
                labelRowsPerPage="Số bản ghi mỗi trang:"
            />

            {shouldOpenDialog && (
                <ReligionDialog
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
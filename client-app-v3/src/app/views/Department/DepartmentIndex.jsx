import React, { useEffect } from 'react';
import { observer } from 'mobx-react';
import { useStore } from 'app/stores';
import moment from 'moment';
import MaterialTable from 'material-table';
import { Button, Icon, IconButton, TextField, TablePagination, Grid } from '@material-ui/core';
import DepartmentDialog from './DepartmentDialog';
import GlobitsConfirmationDialog from 'app/common/GlobitsConfirmationDialog';

export default observer(function DepartmentIndex() {
    const { departmentStore } = useStore();
    const {
        departmentList,
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
    } = departmentStore;

    useEffect(() => {
        updatePageData();
    }, [updatePageData]);

    const columns = [
        { title: 'Mã phòng ban', field: 'code' },
        { title: 'Tên phòng ban', field: 'name' },
        { title: 'Mô tả', field: 'description' },
        { title: 'Chức năng', field: 'func' },
        {
            title: 'Ngày thành lập',
            field: 'foundedDate',
            render: (rowData) => {
                if (!rowData.foundedDate) return '';
                const d = moment(rowData.foundedDate);
                return d.isValid() ? d.format('DD/MM/YYYY') : '';
            }
        },
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
                        Thêm mới Phòng ban
                    </Button>
                </Grid>
                <Grid item>
                    <TextField
                        label="Tìm kiếm phòng ban..."
                        variant="outlined"
                        size="small"
                        onChange={handleSearchChange}
                        style={{ width: '300px' }}
                    />
                </Grid>
            </Grid>

            <MaterialTable
                title="Danh sách Phòng ban"
                columns={columns}
                data={departmentList.slice()}
                parentChildData={(row, rows) => {
                    // Tree data format: link child to parent
                    if (row.parent && row.parent.id) {
                        return rows.find(a => a.id === row.parent.id);
                    }
                    return null;
                }}
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
                rowsPerPageOptions={[5, 10, 25, 50, 100]}
                labelRowsPerPage="Số bản ghi mỗi trang:"
            />

            {shouldOpenDialog && (
                <DepartmentDialog
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
                    text="Bạn có chắc chắn muốn xóa phòng ban này không?"
                    agree="Xóa"
                    cancel="Hủy"
                />
            )}
        </div>
    );
});
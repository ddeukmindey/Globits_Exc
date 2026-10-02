import React, { useEffect } from 'react';
import { observer } from 'mobx-react';
import { useStore } from 'app/stores';
import MaterialTable from 'material-table';
import { Button, Icon, IconButton, TextField, TablePagination, Grid } from '@material-ui/core';
import FamilyRelationshipDialog from './FamilyRelationshipDialog';
import GlobitsConfirmationDialog from 'app/common/GlobitsConfirmationDialog';

export default observer(function FamilyRelationshipIndex() {
    const { familyRelationshipStore } = useStore();
    const {
        familyRelationshipList,
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
    } = familyRelationshipStore;

    useEffect(() => {
        updatePageData();
    }, [updatePageData]);

    const columns = [
        { title: 'Mã quan hệ', field: 'code' },
        { title: 'Tên quan hệ', field: 'name' },
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
                        Thêm mới Quan hệ gia đình
                    </Button>
                </Grid>
                <Grid item>
                    <TextField
                        label="Tìm kiếm quan hệ gia đình..."
                        variant="outlined"
                        size="small"
                        onChange={handleSearchChange}
                        style={{ width: '300px' }}
                    />
                </Grid>
            </Grid>

            <MaterialTable
                title="Danh sách Quan hệ gia đình"
                columns={columns}
                data={familyRelationshipList.slice()}
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
                <FamilyRelationshipDialog
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
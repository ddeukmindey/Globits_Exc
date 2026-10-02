import React, { useEffect } from 'react';
import { observer } from 'mobx-react';
import { useStore } from 'app/stores';
import GlobitsTable from 'app/common/GlobitsTable';
import GlobitsSearchInput from 'app/common/GlobitsSearchInput';
import ProjectDialog from './ProjectDialog';
import GlobitsConfirmationDialog from 'app/common/GlobitsConfirmationDialog';
import { Button, Grid, Icon, IconButton } from "@material-ui/core";

export default observer(function ProjectIndex() {
    const { projectStore } = useStore();
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
        handleEditItem,
        handleAddItem,
        handleCloseDialog,
        handleDeleteItem,
        handleConfirmDelete,
        handleCloseConfirmationDialog,
        updatePageData,
    } = projectStore;

    useEffect(() => {
        updatePageData();
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
        { title: "Mã dự án", field: "code", align: "center", width: "150" },
        { title: "Tên dự án", field: "name", align: "center", width: "250" },
        { title: "Mô tả", field: "description", align: "center", width: "350" },
        {
            title: "Hành động", field: "custom", align: "center", width: "250",
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
                <Grid container spacing={2} justifyContent='space-between' alignItems='center'>
                    <Grid item lg={6} md={6} sm={6} xs={12}>
                        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start' }}>
                            <h2 style={{ margin: 0, marginBottom: '16px' }}>Quản lý dự án</h2>
                            <Button variant='contained' color='primary' onClick={handleAddItem}>Thêm mới</Button>
                        </div>
                    </Grid>
                    <Grid item lg={6} md={6} sm={6} xs={12}>
                        <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
                            <div style={{ width: '100%', maxWidth: '350px' }}>
                                <GlobitsSearchInput search={handleSearch} />
                            </div>
                        </div>
                    </Grid>
                </Grid>
            </div>
            <GlobitsTable
                data={itemList ? itemList.slice() : []}
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
                <ProjectDialog
                    open={shouldOpenDialog}
                    handleClose={handleCloseDialog}
                />
            )}
            {shouldOpenConfirmationDialog && (
                <GlobitsConfirmationDialog
                    title="Xác nhận xóa"
                    text="Bạn có chắc chắn muốn xóa dự án này không?"
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

import React, { useEffect } from "react";
import { observer } from "mobx-react";
import { useStore } from "app/stores";
import GlobitsSearchInput from "app/common/GlobitsSearchInput";
import GlobitsTable from "app/common/GlobitsTable";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Button,
  Grid,
  Radio,
  Icon,
  IconButton
} from "@material-ui/core";

export default observer(function DepartmentParentPopup() {
  const { departmentStore } = useStore();
  const {
    shouldOpenParentPopup,
    handleCloseParentPopup,
    popupDepartmentList,
    popupTotalElements,
    popupTotalPages,
    popupPage,
    popupPageSize,
    selectedParent,
    handleSelectRadioParent,
    handleConfirmParentOption,
    setPopupPage,
    setPopupPageSize,
    setPopupKeyword,
    updatePopupData
  } = departmentStore;

  // Initialize data on popup open
  useEffect(() => {
    updatePopupData();
  }, [updatePopupData]);

  const handleChangePage = (event, newPage) => {
    setPopupPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setPopupPageSize(Number(event.target.value));
  };

  const handleSearch = (searchObject) => {
    setPopupKeyword(searchObject.keyword);
  };


  const columns = [
    {
      title: "Chọn",
      field: "custom",
      align: "center",
      width: "100",
      render: (rowData) => (
        <Radio
          checked={selectedParent && selectedParent.id === rowData.id}
          onChange={() => handleSelectRadioParent(rowData)}
          value={rowData.id}
          name="radio-button-demo"
        />
      )
    },
    { title: "Mã", field: "code", width: "150" },
    { title: "Tên phòng ban", field: "name", width: "250" },
    { title: "Mô tả", field: "description", width: "250" },
  ];

  return (
    <Dialog open={shouldOpenParentPopup} onClose={handleCloseParentPopup} maxWidth="md" fullWidth={true}>
      <DialogTitle
        style={{ backgroundColor: '#26c6da', color: 'white', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}
      >
        <span>Lựa Chọn Phòng Ban</span>
        <IconButton onClick={handleCloseParentPopup} style={{ color: 'white' }}>
          <Icon>close</Icon>
        </IconButton>
      </DialogTitle>

      <DialogContent dividers>
        <Grid container spacing={2} justifyContent="flex-end" style={{ marginBottom: 10 }}>
          <Grid item lg={4} md={4} sm={6} xs={12}>
            <GlobitsSearchInput search={handleSearch} />
          </Grid>
        </Grid>

        <GlobitsTable
          data={popupDepartmentList.slice()}
          columns={columns}
          totalPages={popupTotalPages}
          handleChangePage={handleChangePage}
          setRowsPerPage={handleChangeRowsPerPage}
          pageSize={popupPageSize}
          pageSizeOption={[5, 10, 25]}
          totalElements={popupTotalElements}
          page={popupPage}
          selection={false}
          handleSelectList={() => { }}
        />
      </DialogContent>

      <DialogActions>
        <Button variant="contained" color="secondary" onClick={handleCloseParentPopup}>
          Hủy
        </Button>
        <Button variant="contained" style={{ backgroundColor: '#26c6da', color: 'white' }} onClick={handleConfirmParentOption}>
          Lựa chọn
        </Button>
      </DialogActions>
    </Dialog>
  );
});

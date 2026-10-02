import { createContext, useContext } from "react";
import CountryStore from "./views/Country/CountryStore";
import EthnicsStore from "./views/Ethnics/EthnicsStore";
import ReligionStore from "./views/Religion/ReligionStore";
import FamilyRelationshipStore from "./views/FamilyRelationship/FamilyRelationshipStore";
import DepartmentStore from "./views/Department/DepartmentStore";
import StaffStore from "./views/Staff/StaffStore";
import ProjectStore from "./views/Project/ProjectStore";
import TimeSheetStore from "./views/TimeSheet/TimeSheetStore";
import CompanyStore from "./views/Company/CompanyStore";
import TaskStore from "./views/Task/TaskStore";

export const store = {
  countryStore: new CountryStore(),
  ethnicsStore: new EthnicsStore(),
  religionStore: new ReligionStore(),
  familyRelationshipStore: new FamilyRelationshipStore(),
  departmentStore: new DepartmentStore(),
  staffStore: new StaffStore(),
  projectStore: new ProjectStore(),
  timeSheetStore: new TimeSheetStore(),
  companyStore: new CompanyStore(),
  taskStore: new TaskStore(),
};

export const StoreContext = createContext(store);

export function useStore() {
  return useContext(StoreContext);
}
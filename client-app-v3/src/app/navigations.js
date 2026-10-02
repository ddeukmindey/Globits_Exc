import ConstantList from "./appConfig";

export const navigations = [
  {
    name: "navigation.dashboard",
    icon: "home",
    path: ConstantList.ROOT_PATH + "dashboard",
    isVisible: true,
  },
  {
    name: "navigation.directory",
    icon: "dashboard",
    isVisible: true,
    children: [
      {
        name: "navigation.country",
        path: ConstantList.ROOT_PATH + "category/country",
        icon: "remove",
        isVisible: true,
      },
      {
        name: "navigation.ethnics",
        path: ConstantList.ROOT_PATH + "category/ethnics",
        icon: "remove",
        isVisible: true,
      },
      {
        name: "navigation.religion",
        path: ConstantList.ROOT_PATH + "category/religion",
        icon: "remove",
        isVisible: true,
      },
      {
        name: "navigation.familyRelationship",
        path: ConstantList.ROOT_PATH + "category/familyRelationship",
        icon: "remove",
        isVisible: true,
      },
      {
        name: "navigation.department",
        path: ConstantList.ROOT_PATH + "category/department",
        icon: "remove",
        isVisible: true,
      },
      {
        name: "navigation.company",
        path: ConstantList.ROOT_PATH + "category/company",
        icon: "remove",
        isVisible: true,
      },
    ],
  },
  {
    name: "navigation.staff_manager",
    icon: "assignment_ind",
    isVisible: true,
    children: [
      {
        name: "navigation.staff",
        path: ConstantList.ROOT_PATH + "staff_manager/staff",
        icon: "remove",
        isVisible: true,
      },
    ],
  },
  {
    name: "navigation.project_manager",
    icon: "assessment",
    isVisible: true,
    children: [
      {
        name: "navigation.project",
        path: ConstantList.ROOT_PATH + "project_manager/project",
        icon: "remove",
        isVisible: true,
      },
      {
        name: "navigation.timesheet",
        path: ConstantList.ROOT_PATH + "project_manager/timesheet",
        icon: "remove",
        isVisible: true,
      },
      {
        name: "navigation.task",
        path: ConstantList.ROOT_PATH + "project_manager/task",
        icon: "remove",
        isVisible: true,
      },
    ],
  },
];
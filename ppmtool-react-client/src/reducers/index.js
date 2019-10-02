import { combineReducers } from "redux";
import errorReducer from "./errorReducer";
import projectReducer from "./projectReducer";
import backlogReducer from "./backlogReducer";

// combineReducers is the main reducer that goes into our store
export default combineReducers({
  errors: errorReducer,
  project: projectReducer,
  backlog: backlogReducer
});

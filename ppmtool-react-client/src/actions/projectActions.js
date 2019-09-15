import axios from "axios";
import { GET_ERRORS } from "./types";

// async means that the function always returns a promise
// we will use it with await
// it means that js will wait until that promise settles and returns its result
export const createProject = (project, history) => async dispatch => {
  try {
    const res = await axios.post("http://localhost:8080/api/project", project);
    history.push("/dashboard");
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data
    });
  }
};

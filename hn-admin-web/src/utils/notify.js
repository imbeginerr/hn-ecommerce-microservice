import { ElMessage } from 'element-plus';

export function notifySuccess(message) {
  ElMessage({ message, type: 'success', duration: 2600, showClose: true });
}

export function notifyError(message) {
  ElMessage({ message, type: 'error', duration: 4200, showClose: true });
}

export function notifyInfo(message) {
  ElMessage({ message, type: 'info', duration: 2600, showClose: true });
}

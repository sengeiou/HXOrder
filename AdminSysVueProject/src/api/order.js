import request from '@/utils/request'
export function fetchList(params) {
  return request({
    url:'/order/list',
    method:'get',
    params:params
  })
}


export function deleteOrder(params) {
  return request({
    url:'/order/delete',
    method:'post',
    params:params
  })
}

export function sendMessage(data) {
  return request({
    url:'/message/send',
    method:'post',
    data:data
  });
}

export function getOrderDetail(id) {
  return request({
    url:'/order/get',
    params: {
      orderId: id
    },
    method:'get'
  });
}

export function updateOrderType(oid, nowConsumeType, nowOrderType) {
  return request({
    url:'/order/update_type',
    method:'post',
    params: {
      orderId: oid,
      nowOrderType: nowOrderType,
      nowConsumeType: nowConsumeType
    }
  });
}

export function rejectOrder(oid, uid, sid, reason) {
  return request({
    url:'/order/reject_confirm',
    method:'post',
    params: {
      orderId: oid,
      userId: uid,
      storeId: sid,
      rejectReason: reason
    }
  });
}



export function hadnleToBePaidOrder(data) {
  return request({
    url:'/order/handle_to_be_paid_order',
    method:'post',
    data:data
  });
}





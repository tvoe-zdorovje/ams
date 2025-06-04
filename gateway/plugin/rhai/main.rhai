import "permissionChecker" as permissionChecker;

fn process_request(request) {
  permissionChecker::process_request(request)
}

fn supergraph_service(service) {
  // Rhai convention for creating a function pointer
  const request_callback = Fn("process_request");

  service.map_request(request_callback);
}

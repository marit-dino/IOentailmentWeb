export interface Response {
  entails: boolean,
  counterModelWorlds?: Map<string, Map<number, boolean>>
  counterModelClassical?: Map<string, boolean>
}
